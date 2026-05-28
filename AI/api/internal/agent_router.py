from graph.state import AgentState


def route_input(state: AgentState) -> str:
    """
    LangGraph 조건부 엣지 함수.
    state의 input_type을 보고 다음 노드 이름을 반환한다.
    """
    input_type = state["input_type"]

    if input_type == "image_plan":
        return "vlm_node"
    elif input_type == "nfc":
        return "nfc_node"  # nfc_url → Gemini URL 분석
    elif input_type == "preference":
        return "agent_node"  # 전처리 없이 바로 agent로
    else:
        raise ValueError(f"알 수 없는 input_type: {input_type}")
