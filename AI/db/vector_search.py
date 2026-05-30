"""
Supabase pgvector RPC 함수 4종을 Python에서 타입 안전하게 호출하기 위한 wrapper.

모든 RPC는 service_role 권한으로 실행되며, get_supabase()를 통해서만 접근한다.
TypedDict로 반환 타입을 명시해 IDE 자동완성과 타입 체크를 지원한다.
"""
from typing import List, Optional, TypedDict

from db.supabase_client import get_supabase


class ProductMatch(TypedDict):
    product_id: str
    similarity: float


class ReviewMatch(TypedDict):
    review_id: str
    review_text: str
    similarity: float


class ContextMatch(TypedDict):
    context_text: str
    similarity: float
    created_at: str


def match_products(
    query_embedding: List[float],
    category: Optional[str] = None,
    match_count: int = 20,
    exclude_ids: Optional[List[str]] = None,
) -> List[ProductMatch]:
    """
    discovery_agent용 후보 상품 검색.

    Args:
        query_embedding: intent_vector (1024차원)
        category: None이면 전체 카테고리 검색. "base"/"sun"/"lip"/"skincare"
        match_count: 반환할 후보 수
        exclude_ids: 제외할 상품 ID (기준 상품, 이미 추천된 상품 등)
    """
    sb = get_supabase()
    res = sb.rpc(
        "match_products",
        {
            "query_embedding": query_embedding,
            "match_category": category,
            "match_count": match_count,
            "exclude_ids": exclude_ids or [],
        },
    ).execute()
    return res.data or []


def match_alternatives(
    base_product_id: str,
    match_count: int = 5,
    exclude_ids: Optional[List[str]] = None,
) -> List[ProductMatch]:
    """
    alternative_agent용 대체 상품 검색.

    base_product의 feature_vec과 같은 카테고리 내 유사 상품을 코사인 거리로 검색.
    LLM 호출 없이 순수 벡터 검색.
    """
    sb = get_supabase()
    res = sb.rpc(
        "match_alternatives",
        {
            "base_product_id": base_product_id,
            "match_count": match_count,
            "exclude_ids": exclude_ids or [],
        },
    ).execute()
    return res.data or []


def match_reviews(
    query_embedding: List[float],
    product_id: str,
    match_count: int = 10,
) -> List[ReviewMatch]:
    """
    score_agent의 review_score 계산용.

    특정 상품의 리뷰 중 intent_vector와 가장 가까운 N개 반환.
    상위 N개 유사도 평균이 review_score가 된다.
    """
    sb = get_supabase()
    res = sb.rpc(
        "match_reviews",
        {
            "query_embedding": query_embedding,
            "target_product_id": product_id,
            "match_count": match_count,
        },
    ).execute()
    return res.data or []


def match_user_contexts(
    user_id: str,
    query_embedding: List[float],
    category: Optional[str] = None,
    match_count: int = 5,
) -> List[ContextMatch]:
    """
    discovery_agent의 RAG 컨텍스트 검색.

    user_context_rags에서 해당 사용자의 과거 조회 맥락 중 query_embedding과
    가장 가까운 N개를 반환. unified_text_builder의 rag_contexts 입력으로 사용.
    """
    sb = get_supabase()
    res = sb.rpc(
        "match_user_contexts",
        {
            "target_user_id": user_id,
            "query_embedding": query_embedding,
            "match_category": category,
            "match_count": match_count,
        },
    ).execute()
    return res.data or []