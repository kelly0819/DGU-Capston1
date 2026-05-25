from typing import Optional, List, Dict, Literal
from pydantic import BaseModel, Field
from datetime import datetime


# =========================================
# 카테고리 타입 (자주 쓰일 거)
# =========================================

CategoryMain = Literal["base_makeup", "skincare", "lip_eye"]


# =========================================
# 상품 등록 (POST /products/upsert)
# =========================================

class ProductUpsertRequest(BaseModel):
    """VLM팀/Agent2가 분석 끝낸 상품을 등록할 때 보내는 형식"""
    product_id: Optional[int] = Field(
        None, description="없으면 새로 만듦. 있으면 그 ID로 갱신"
    )
    name: str
    brand_name: str
    category_main: CategoryMain
    category_sub: str = Field(..., description="liquid_foundation, sleeping_mask 등")
    skin_type: Optional[str] = Field(None, description="이 상품이 적합한 피부 타입")
    image_url: Optional[str] = None
    price: Optional[int] = None
    rating: Optional[float] = None
    review_summary: Optional[str] = None
    feature_json: Dict[str, float] = Field(
        ..., description="카테고리별 feature. 예: {matte: 0.8, glow: 0.2, ...}"
    )


class ProductUpsertResponse(BaseModel):
    product_id: int
    created: bool = Field(..., description="새로 만들어졌으면 True, 갱신이면 False")


# =========================================
# 사용자 벡터 조회 (GET /users/{user_id}/vector)
# =========================================

class UserVectorResponse(BaseModel):
    user_id: int
    category: CategoryMain
    vector: List[float]
    skin_type: Optional[str]
    personal_color: Optional[str]


# =========================================
# 콘텐츠 기반 추천 (POST /search/similar-products)
# =========================================

class SimilarProductsRequest(BaseModel):
    user_vector: List[float] = Field(..., description="검색 기준이 되는 벡터")
    category: CategoryMain
    top_k: int = 10
    price_max: Optional[int] = None


class ProductSearchResult(BaseModel):
    product_id: int
    score: float
    name: str
    brand_name: str
    price: Optional[int]


class SimilarProductsResponse(BaseModel):
    results: List[ProductSearchResult]


# =========================================
# 협업 필터링 (POST /search/similar-users)
# =========================================

class SimilarUsersRequest(BaseModel):
    user_id: int
    category: CategoryMain
    top_k: int = 20


class UserSearchResult(BaseModel):
    user_id: int
    score: float


class SimilarUsersResponse(BaseModel):
    similar_users: List[UserSearchResult]


# =========================================
# RAG 구매 사유 검색 (POST /reasons/retrieve)
# =========================================

class ReasonRetrieveRequest(BaseModel):
    user_id: int
    category: CategoryMain
    query_text: str
    top_k: int = 5


class ReasonResult(BaseModel):
    rag_id: int
    text: str
    score: float
    created_at: datetime


class ReasonRetrieveResponse(BaseModel):
    reasons: List[ReasonResult]


# =========================================
# 구매 사유 추가 (POST /users/{user_id}/reasons)
# =========================================

class ReasonAddRequest(BaseModel):
    content: str
    category: CategoryMain


class ReasonAddResponse(BaseModel):
    rag_id: int