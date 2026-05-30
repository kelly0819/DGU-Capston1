"""
discovery_agent — 추천 파이프라인의 진입 에이전트.

사용자의 현재 요청 의도와 과거 맥락(RAG)을 결합해 통합 자연어를 만들고,
bge-m3로 임베딩해 intent_vector를 생성한 뒤 pgvector로 후보 상품을 탐색한다.

후속 에이전트(score/alternative/collaborative)가 공통으로 사용하는
intent_vector와 candidates를 생산하므로 반드시 가장 먼저 실행된다.

LLM 호출 없음 (임베딩 + 벡터 검색만).
"""
from __future__ import annotations

import asyncio
from typing import List, Optional, TypedDict

from db.product_reader import get_product_meta_with_vec
from db.vector_search import ProductMatch, match_products, match_user_contexts
from models.agent_context import UserProfile
from models.extracted_product import ExtractedProduct
from services.embedding_service import EmbeddingService
from services.unified_text_builder import build_unified_text


class DiscoveryResult(TypedDict):
    intent_vector: List[float]
    candidates: List[ProductMatch]


async def run_discovery(
    user_profile: UserProfile,
    base_product_id: str,
    search_purpose: Optional[str] = None,
    price_tolerance_percent: Optional[int] = None,
    query: Optional[str] = None,
    rag_count: int = 5,
    top_k: int = 20,
) -> DiscoveryResult:
    
    emb = EmbeddingService.get()

    # 1. 기준 상품 메타 + feature_vec 조회
    base_meta = await asyncio.to_thread(get_product_meta_with_vec, base_product_id)
    if base_meta is None:
        raise ValueError(f"기준 상품을 찾을 수 없습니다: {base_product_id}")
    category = base_meta["category"]

    # 2. DB에 저장된 feature_vec을 RAG 검색용 벡터로 재사용
    #    feature_json 기반으로 사전 생성된 의미 벡터를 그대로 사용.
    #    user_context_rags.embedding과 동일한 모델(bge-m3)·차원(1024)이므로
    #    코사인 유사도 계산이 의미적으로 올바르게 동작한다.
    base_vec: List[float] = base_meta["feature_vec"]

    # 3. RAG 컨텍스트 조회
    rag_matches = await asyncio.to_thread(
        match_user_contexts,
        user_profile.user_id,
        base_vec,
        category,
        rag_count,
    )
    rag_contexts = [m["context_text"] for m in rag_matches]

    # 4. 통합 자연어 생성 (unified_text_builder는 ExtractedProduct를 받음)
    base_product = ExtractedProduct(
        product_name=base_meta["name"],
        brand=base_meta["brand"],
        category={"main": category, "sub": None},
    )
    unified = build_unified_text(
        user_profile=user_profile,
        base_product=base_product,
        search_purpose=search_purpose,
        price_tolerance_percent=price_tolerance_percent,
        rag_contexts=rag_contexts,
        query=query,
    )

    # 5. intent_vector 생성
    intent_vector = await asyncio.to_thread(emb.embed, unified)

    # 6. 후보 검색 (기준 상품 제외)
    candidates = await asyncio.to_thread(
        match_products,
        intent_vector,
        category,
        top_k,
        [base_product_id],
    )

    return DiscoveryResult(intent_vector=intent_vector, candidates=candidates)


# @tool 래퍼
# Action Model 등록용. 오케스트레이션 담당(LLM/VLM)과 협의해 graph/action_model.py
# 또는 agents/tools.py에서 등록한다.
#
# 주의: intent_vector(1024차원)는 LLM에 직접 노출하면 토큰 낭비이므로,
#      반환 dict는 candidates만 LLM에 전달하고 intent_vector는 공유 상태
#      (AgentState/AgentContext)에 저장해 score_agent가 읽도록 한다.
#      아래 래퍼는 full result를 반환하므로, 오케스트레이터가 state에 넣을 때
#      intent_vector를 LLM 메시지에서 제외하는 처리를 해야 한다.

try:
    from langchain_core.tools import tool

    @tool
    async def call_discovery_agent(
        user_id: str,
        base_product_id: str,
        skin_type: Optional[str] = None,
        personal_color: Optional[str] = None,
        skin_concerns: Optional[List[str]] = None,
        search_purpose: Optional[str] = None,
        price_tolerance_percent: Optional[int] = None,
        query: Optional[str] = None,
    ) -> dict:
        """사용자 프로필과 기준 상품으로 후보 상품군을 탐색한다.
        intent_vector(이후 점수 계산에 재사용)와 candidates(후보 상품 목록)를 생산한다.
        추천 파이프라인에서 가장 먼저 호출해야 한다."""
        profile = UserProfile(
            user_id=user_id,
            skin_type=skin_type,
            personal_color=personal_color,
            skin_concerns=skin_concerns or [],
        )
        result = await run_discovery(
            user_profile=profile,
            base_product_id=base_product_id,
            search_purpose=search_purpose,
            price_tolerance_percent=price_tolerance_percent,
            query=query,
        )
        return {
            "intent_vector": result["intent_vector"],  # state로만 라우팅, LLM 노출 금지
            "candidates": result["candidates"],
        }

except ImportError:
    # langchain_core 미설치 환경(단위 테스트 등)에서는 @tool 래퍼 생략
    call_discovery_agent = None  # type: ignore