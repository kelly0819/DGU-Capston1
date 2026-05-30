"""
discovery_agent의 intent_vector 생성을 위한 통합 자연어 빌더.

다음 요소를 결합한다:
- UserProfile (피부타입, 퍼스널컬러, 피부고민)
- searchPurpose (사용 목적)
- RAG 컨텍스트 (과거 조회 맥락 N개)
- baseProduct (기준 상품 — LLM/VLM 담당의 ExtractedProduct)
- query (사용자 자유 텍스트, 선택)
- priceTolerancePercent (예산 허용 폭, 선택)

생성된 통합 자연어는 bge-m3로 임베딩되어 intent_vector(1024차원)가 된다.
"""
from typing import List, Optional

from models.agent_context import UserProfile
from models.extracted_product import ExtractedProduct


_PURPOSE_LABEL = {
    "DAILY": "데일리",
    "GIFT": "선물용",
    "TRAVEL": "여행용",
    "SPECIAL": "특별한 날",
    "OFFICE": "오피스",
    "DATE": "데이트",
}


def build_unified_text(
    user_profile: UserProfile,
    base_product: ExtractedProduct,
    search_purpose: Optional[str] = None,
    price_tolerance_percent: Optional[int] = None,
    rag_contexts: Optional[List[str]] = None,
    query: Optional[str] = None,
) -> str:
    """
    통합 자연어 생성. None인 필드는 자동 제외.

    LLM/VLM의 ExtractedProduct는 product_name(str), brand(str), category(dict)
    형태이므로 category["main"]으로 접근.
    """
    parts: List[str] = []

    # 1. 사용자 프로필
    profile_parts: List[str] = []
    if user_profile.skin_type:
        profile_parts.append(f"{user_profile.skin_type} 피부")
    if user_profile.personal_color:
        profile_parts.append(f"{user_profile.personal_color} 톤")
    if user_profile.skin_concerns:
        concerns = "·".join(user_profile.skin_concerns)
        profile_parts.append(f"{concerns} 고민")
    if profile_parts:
        parts.append(", ".join(profile_parts) + "을 가진 사용자.")

    # 2. RAG 컨텍스트 (과거 맥락)
    if rag_contexts:
        joined = " / ".join(rag_contexts)
        parts.append(f"과거에 다음과 같은 제품을 찾아봄: {joined}.")

    # 3. 사용 목적
    if search_purpose:
        label = _PURPOSE_LABEL.get(search_purpose, search_purpose)
        parts.append(f"이번에는 {label} 용도로 추천을 원함.")

    # 4. 기준 상품
    product_name = base_product.product_name or "상품"
    brand = base_product.brand or ""
    category_main = ""
    if base_product.category and isinstance(base_product.category, dict):
        category_main = base_product.category.get("main", "")

    base_text = f"기준 상품은 {brand} {product_name}".strip()
    if category_main:
        base_text += f" ({category_main} 카테고리)"
    if price_tolerance_percent is not None:
        base_text += f"이며 가격 허용 폭은 ±{price_tolerance_percent}%"
    parts.append(base_text + ".")

    # 5. 자유 텍스트 사유
    if query:
        parts.append(f"구매 사유: {query}")

    return "\n".join(parts)