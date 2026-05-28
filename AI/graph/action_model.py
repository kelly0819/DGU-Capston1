import httpx
from langchain_core.messages import HumanMessage, SystemMessage
from langchain_google_genai import ChatGoogleGenerativeAI
from langgraph.graph import StateGraph, END
from langgraph.prebuilt import ToolNode

from config import settings
from graph.state import AgentState
from api.internal.agent_router import route_input
from agents.input_agent.image_parser import parse_image
from agents.input_agent.nfc_parser import parse_nfc_url
from agents.tools import ALL_TOOLS


# ── 오케스트레이터 LLM ────────────────────────────────────────────────────
# Gemini가 tool_calls를 보고 어떤 도구를 호출할지 스스로 결정한다

_llm = ChatGoogleGenerativeAI(
    model=settings.GEMINI_MODEL,
    google_api_key=settings.GEMINI_API_KEY,
    temperature=0,
)
_llm_with_tools = _llm.bind_tools(ALL_TOOLS)

SYSTEM_PROMPT = """당신은 화장품 추천 에이전트입니다.

## 도구 호출 규칙

### product_info가 있을 때 (시나리오 1, 2 - 특정 상품 분석):
1. gemini_price_review  → 최저가 + 리뷰 수집
2. search_rag           → 유사 상품 검색
3. compute_matching_score → 매칭 점수 계산
4. collab_filter        → 대체 상품 추천

### product_info가 없을 때 (시나리오 3 - 성향 맞춤 추천):
- DB 데이터 충분: collab_filter → search_rag → compute_matching_score
- Cold Start:     gemini_web_search → save_feature_vector → collab_filter → search_rag → compute_matching_score

## 완료 조건
모든 도구 호출이 끝나면 도구를 더 이상 호출하지 말고 "추천 완료"라고만 답하세요.
"""


# ── 전처리 노드 ───────────────────────────────────────────────────────────

def vlm_node(state: AgentState) -> dict:
    """시나리오 1: Qwen VLM으로 이미지 분석 후 agent에 넘김"""
    image_bytes = httpx.get(state["image_url"]).content
    product_info = parse_image(image_bytes)

    intro = f"이미지에서 {product_info.brand} {product_info.product_name}을 찾았습니다."
    if state.get("purchase_plan"):
        intro += f" 사용자 구매 계획: {state['purchase_plan']}"
    intro += " 이 제품을 분석해주세요."

    return {
        "product_info": product_info,
        "messages": [HumanMessage(content=intro)],
    }


def nfc_node(state: AgentState) -> dict:
    """시나리오 2: 올리브영 URL을 Gemini로 분석해서 상품 정보 추출"""
    product_info = parse_nfc_url(state["nfc_url"])

    intro = f"올리브영 페이지에서 {product_info.brand} {product_info.product_name}을 찾았습니다. 이 제품을 분석해주세요."
    return {
        "product_info": product_info,
        "messages": [HumanMessage(content=intro)],
    }


# ── ReAct 핵심 노드 ───────────────────────────────────────────────────────

def agent_node(state: AgentState) -> dict:
    """오케스트레이터: LLM이 tool_calls를 생성하거나 '추천 완료'로 종료"""
    messages = [SystemMessage(content=SYSTEM_PROMPT)] + state["messages"]
    response = _llm_with_tools.invoke(messages)
    return {"messages": [response]}


def should_continue(state: AgentState) -> str:
    """tool_calls가 있으면 tool_node로, 없으면 build_response로"""
    last = state["messages"][-1]
    if getattr(last, "tool_calls", None):
        return "tool_node"
    return "build_response"


def build_response_node(state: AgentState) -> dict:
    """도구 실행 결과를 모아 final_output 구성"""
    tool_results = [
        m.content
        for m in state["messages"]
        if m.type == "tool"
    ]
    return {"final_output": {"results": tool_results}}


# ── 그래프 조립 ───────────────────────────────────────────────────────────

def build_graph():
    graph = StateGraph(AgentState)

    graph.add_node("vlm_node", vlm_node)
    graph.add_node("nfc_node", nfc_node)
    graph.add_node("agent_node", agent_node)
    graph.add_node("tool_node", ToolNode(ALL_TOOLS))
    graph.add_node("build_response", build_response_node)

    # 입력 유형에 따라 전처리 노드 or 바로 agent로
    graph.add_conditional_edges(
        "__start__",
        route_input,
        {
            "vlm_node": "vlm_node",
            "nfc_node": "nfc_node",
            "agent_node": "agent_node",
        }
    )

    # 전처리 완료 → agent
    graph.add_edge("vlm_node", "agent_node")
    graph.add_edge("nfc_node", "agent_node")

    # ReAct 루프: agent → tool → agent → ... → build_response
    graph.add_conditional_edges(
        "agent_node",
        should_continue,
        {
            "tool_node": "tool_node",
            "build_response": "build_response",
        }
    )
    graph.add_edge("tool_node", "agent_node")
    graph.add_edge("build_response", END)

    return graph.compile()


agent_graph = build_graph()
