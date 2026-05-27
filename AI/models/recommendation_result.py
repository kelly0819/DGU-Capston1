"""
추천 결과 모델. AI 추천 결과의 스키마(schema).

Action Model 실행 완료 후 recommendation_jobs.result JSONB에 저장.
Spring이 GET /recommendations/{jobId} 응답으로 그대로 내려준다.
"""
from typing import List, Optional

from pydantic import BaseModel, ConfigDict, Field


class SimilarUserProduct(BaseModel):
    """협업 필터링 결과 항목. collaborative_agent 출력."""

    model_config = ConfigDict(populate_by_name=True)

    id: str
    name: str
    brand: str
    image_url: Optional[str] = Field(None, alias="imageUrl")
    price: Optional[int] = None
    satisfaction_percent: int = Field(..., alias="satisfactionPercent", ge=0, le=100)


class AlternativeProduct(BaseModel):
    """대체 상품 결과 항목. alternative_agent 출력."""

    model_config = ConfigDict(populate_by_name=True)

    id: str
    name: str
    brand: str
    image_url: Optional[str] = Field(None, alias="imageUrl")
    price: Optional[int] = None
    ingredient_similarity: int = Field(
        ..., alias="ingredientSimilarity", ge=0, le=100,
    )


class RecommendationResult(BaseModel):
    """최종 추천 결과."""

    model_config = ConfigDict(populate_by_name=True)

    match_score: int = Field(..., alias="matchScore", ge=0, le=100)
    match_label: str = Field(..., alias="matchLabel")
    ai_reason: str = Field(..., alias="aiReason")
    similar_user_products: List[SimilarUserProduct] = Field(
        default_factory=list, alias="similarUserProducts"
    )
    alternative_products: List[AlternativeProduct] = Field(
        default_factory=list, alias="alternativeProducts"
    )