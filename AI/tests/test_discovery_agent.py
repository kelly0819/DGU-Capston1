"""discovery_agent 핵심 로직 테스트. 의존 모듈은 모두 모킹."""
from unittest.mock import MagicMock, patch

import pytest

from agents.discovery_agent import run_discovery
from models.agent_context import UserProfile


def _profile():
    return UserProfile(
        user_id="u1",
        skin_type="OILY",
        personal_color="SPRING_WARM",
        skin_concerns=["ACNE"],
    )


def _base_meta():
    return {
        "id": "base-id",
        "name": "네오쿠션",
        "brand": "LANEIGE",
        "category": "base",
        "image_url": None,
        "price": 30000,
        "feature_vec": [0.2] * 1024,
    }


class TestDiscoveryAgent:
    @pytest.mark.asyncio
    @patch("agents.discovery_agent.match_products")
    @patch("agents.discovery_agent.match_user_contexts")
    @patch("agents.discovery_agent.get_product_meta_with_vec")
    @patch("agents.discovery_agent.EmbeddingService")
    async def test_run_discovery_flow(
        self, mock_emb_cls, mock_get_meta, mock_match_ctx, mock_match_prod
    ):
        mock_emb = MagicMock()
        mock_emb.embed.return_value = [0.1] * 1024
        mock_emb_cls.get.return_value = mock_emb

        mock_get_meta.return_value = _base_meta()
        mock_match_ctx.return_value = [
            {"context_text": "과거 세미매트 쿠션 조회", "similarity": 0.9, "created_at": "t"}
        ]
        mock_match_prod.return_value = [
            {"product_id": "p1", "similarity": 0.95},
            {"product_id": "p2", "similarity": 0.88},
        ]

        result = await run_discovery(
            user_profile=_profile(),
            base_product_id="base-id",
            search_purpose="DAILY",
            top_k=20,
        )

        # intent_vector 차원
        assert len(result["intent_vector"]) == 1024
        # candidates 전달
        assert result["candidates"][0]["product_id"] == "p1"

    @pytest.mark.asyncio
    @patch("agents.discovery_agent.match_products")
    @patch("agents.discovery_agent.match_user_contexts")
    @patch("agents.discovery_agent.get_product_meta_with_vec")
    @patch("agents.discovery_agent.EmbeddingService")
    async def test_base_product_excluded(
        self, mock_emb_cls, mock_get_meta, mock_match_ctx, mock_match_prod
    ):
        mock_emb = MagicMock()
        mock_emb.embed.return_value = [0.1] * 1024
        mock_emb_cls.get.return_value = mock_emb
        mock_get_meta.return_value = _base_meta()
        mock_match_ctx.return_value = []
        mock_match_prod.return_value = []

        await run_discovery(user_profile=_profile(), base_product_id="base-id")

        # match_products 호출 시 exclude_ids에 기준 상품 포함
        args = mock_match_prod.call_args.args
        exclude_ids = args[3]
        assert "base-id" in exclude_ids

    @pytest.mark.asyncio
    @patch("agents.discovery_agent.get_product_meta_with_vec")
    @patch("agents.discovery_agent.EmbeddingService")
    async def test_base_product_not_found_raises(self, mock_emb_cls, mock_get_meta):
        mock_emb_cls.get.return_value = MagicMock()
        mock_get_meta.return_value = None

        with pytest.raises(ValueError):
            await run_discovery(user_profile=_profile(), base_product_id="nope")

    @pytest.mark.asyncio
    @patch("agents.discovery_agent.match_products")
    @patch("agents.discovery_agent.match_user_contexts")
    @patch("agents.discovery_agent.get_product_meta_with_vec")
    @patch("agents.discovery_agent.EmbeddingService")
    async def test_category_used_for_candidate_filter(
        self, mock_emb_cls, mock_get_meta, mock_match_ctx, mock_match_prod
    ):
        mock_emb = MagicMock()
        mock_emb.embed.return_value = [0.1] * 1024
        mock_emb_cls.get.return_value = mock_emb
        mock_get_meta.return_value = _base_meta()  # category = "base"
        mock_match_ctx.return_value = []
        mock_match_prod.return_value = []

        await run_discovery(user_profile=_profile(), base_product_id="base-id")

        # match_products의 category 인자가 기준 상품 카테고리("base")인지
        args = mock_match_prod.call_args.args
        assert args[1] == "base"