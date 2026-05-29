"""
alternative_agent — 기준 상품의 대체 상품 추천.

기준 상품(baseProductId)의 feature_vec과 같은 카테고리 안에서 가장 유사한
상품을 코사인 거리로 검색한다. score_agent의 추천이 사용자 의도(intent_vector)
기반이라면, alternative_agent는 상품 자체의 물성 유사도(feature_vec) 기반이다.

LLM 호출 없는 순수 벡터 검색 에이전트.
"""
from __future__ import annotations

import asyncio
from typing import List, Optional

from db.product_reader import get_products_meta
from db.vector_search import match_alternatives
from models.recommendation_result import AlternativeProduct


async def run_alternative(
    base_product_id: str,
    exclude_ids: Optional[List[str]] = None,
    top_k: int = 5,
) -> List[AlternativeProduct]:
    """
    alternative 핵심 로직.

    흐름:
      1. match_alternatives로 같은 카테고리 유사 상품 검색
         (기준 상품 자신 + exclude_ids 제외는 RPC 내부에서 처리)
      2. 결과 상품들의 메타(name, brand, image, price) 배치 조회
      3. AlternativeProduct 리스트로 조립
         ingredient_similarity = round(similarity * 100)

    exclude_ids에는 discovery_agent의 candidates productId 목록을 넣어
    메인 추천과 중복되지 않게 한다.
    """
    matches = await asyncio.to_thread(
        match_alternatives,
        base_product_id,
        top_k,
        exclude_ids or [],
    )
    if not matches:
        return []

    product_ids = [m["product_id"] for m in matches]
    metas = await asyncio.to_thread(get_products_meta, product_ids)

    results: List[AlternativeProduct] = []
    for m in matches:
        meta = metas.get(m["product_id"])
        if meta is None:
            # 메타 없는 상품(데이터 정합성 문제)은 건너뜀
            continue
        results.append(
            AlternativeProduct(
                id=meta["id"],
                name=meta["name"],
                brand=meta["brand"],
                image_url=meta["image_url"],
                price=meta["price"],
                ingredient_similarity=max(0, min(100, round(m["similarity"] * 100))),
            )
        )
    return results


# @tool 래퍼 
# Action Model 등록용. 오케스트레이션 담당과 협의해 등록.

try:
    from langchain_core.tools import tool

    @tool
    async def call_alternative_agent(
        base_product_id: str,
        exclude_product_ids: Optional[List[str]] = None,
        top_k: int = 5,
    ) -> list:
        """기준 상품과 성분·제형이 유사한 대체 상품을 추천한다.
        사용자 의도가 아닌 상품 자체의 물성 유사도(feature_vec) 기반 검색이며
        LLM 호출 없이 벡터 검색만 사용한다."""
        results = await run_alternative(
            base_product_id=base_product_id,
            exclude_ids=exclude_product_ids,
            top_k=top_k,
        )
        # Pydantic 모델 → dict (camelCase alias) 직렬화
        return [r.model_dump(by_alias=True) for r in results]

except ImportError:
    call_alternative_agent = None  # type: ignore