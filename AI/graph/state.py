from typing import TypedDict, Literal, Optional, Annotated
from langgraph.graph.message import add_messages
from models.extracted_product import ExtractedProduct, UserQuery


class AgentState(TypedDict):
    # ── ReAct 메시지 히스토리 ──────────────────────────────
    # add_messages: 덮어쓰지 않고 리스트에 append되도록 처리
    messages: Annotated[list, add_messages]

    # ── 입력 ──────────────────────────────────────────────
    input_type: Literal["image_plan", "nfc", "preference"]

    # 시나리오 1: 이미지 + 구매 계획
    image_url: Optional[str]
    purchase_plan: Optional[str]

    # 시나리오 2: NFC 태그 (스캔 시 올리브영 URL 반환)
    nfc_url: Optional[str]

    # 시나리오 3: 성향 맞춤
    user_query: Optional[UserQuery]

    # ── 전처리 결과 (VLM / NFC 노드가 채워줌) ────────────────
    product_info: Optional[ExtractedProduct]

    # ── 최종 출력 ─────────────────────────────────────────
    final_output: Optional[dict]
