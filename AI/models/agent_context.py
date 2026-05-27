"""
에이전트 간 공유 상태.

ReAct 루프 1회 동안 여러 에이전트가 공유하는 컨텍스트.
가장 중요한 것은 intent_vector: discovery_agent가 한 번 생성하고
score_agent가 review_score 계산에 재사용한다.
"""
from typing import List, Optional

from pydantic import BaseModel, Field

from models.extracted_product import ExtractedProduct  # LLM/VLM 담당이 정의


class UserProfile(BaseModel):
    """사용자 피부·취향 정보. Spring의 user_skin_profiles 테이블 기반."""

    user_id: str
    skin_type: Optional[str] = Field(
        None,
        description="DRY | NORMAL | OILY | COMBINATION | DEHYDRATED_OILY",
    )
    personal_color: Optional[str] = Field(
        None,
        description="SPRING_WARM | SUMMER_COOL | AUTUMN_MUTE | WINTER_COOL | UNKNOWN",
    )
    skin_concerns: List[str] = Field(
        default_factory=list,
        description="SENSITIVITY | ACNE | ATOPY | WHITENING | SEBUM | DARK_CIRCLE 등",
    )
    notes: Optional[List[str]] = Field(None, description="자유 텍스트 특이사항")


class AgentContext(BaseModel):
    """에이전트 간 공유 컨텍스트. ReAct 루프 1회 동안 유효."""

    # === 입력 ===
    user_profile: UserProfile
    base_product: ExtractedProduct
    search_purpose: Optional[str] = Field(
        None, description="DAILY | GIFT | TRAVEL | SPECIAL"
    )
    price_tolerance_percent: Optional[int] = Field(None, ge=0, le=100)
    query: Optional[str] = Field(None, description="사용자 자유 텍스트 구매 사유")

    # === 처리 결과 (에이전트가 단계별로 채워나감) ===
    intent_vector: Optional[List[float]] = Field(
        None,
        description="discovery_agent가 생성. score_agent의 review_score에 재사용",
    )
    unified_text: Optional[str] = Field(
        None, description="discovery_agent가 만든 통합 자연어 (디버깅·로그용)"
    )