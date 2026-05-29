"""alternative_agent 핵심 로직 테스트. 의존 모듈 모킹."""
from unittest.mock import patch

import pytest

from agents.alternative_agent import run_alternative


def _meta(pid, name, brand, price=25000):
    return {
        "id": pid,
        "name": name,
        "brand": brand,
        "category": "base",
        "image_url": None,
        "price": price,
    }


class TestAlternativeAgent:
    @pytest.mark.asyncio
    @patch("agents.alternative_agent.get_products_meta")
    @patch("agents.alternative_agent.match_alternatives")
    async def test_run_alternative_maps_results(self, mock_match_alt, mock_get_metas):
        mock_match_alt.return_value = [
            {"product_id": "p1", "similarity": 0.92},
            {"product_id": "p2", "similarity": 0.85},
        ]
        mock_get_metas.return_value = {
            "p1": _meta("p1", "올아워 파운데이션", "ETUDE"),
            "p2": _meta("p2", "쿠션 매트", "HERA"),
        }

        results = await run_alternative("base-id", top_k=5)

        assert len(results) == 2
        assert results[0].id == "p1"
        assert results[0].name == "올아워 파운데이션"
        # similarity → ingredient_similarity (0~100)
        assert results[0].ingredient_similarity == 92
        assert results[1].ingredient_similarity == 85

    @pytest.mark.asyncio
    @patch("agents.alternative_agent.get_products_meta")
    @patch("agents.alternative_agent.match_alternatives")
    async def test_empty_matches_returns_empty(self, mock_match_alt, mock_get_metas):
        mock_match_alt.return_value = []
        results = await run_alternative("base-id")
        assert results == []
        # 매치 없으면 메타 조회도 안 함
        mock_get_metas.assert_not_called()

    @pytest.mark.asyncio
    @patch("agents.alternative_agent.get_products_meta")
    @patch("agents.alternative_agent.match_alternatives")
    async def test_missing_meta_skipped(self, mock_match_alt, mock_get_metas):
        mock_match_alt.return_value = [
            {"product_id": "p1", "similarity": 0.92},
            {"product_id": "p_ghost", "similarity": 0.80},  # 메타 없음
        ]
        mock_get_metas.return_value = {"p1": _meta("p1", "쿠션A", "브랜드A")}

        results = await run_alternative("base-id")
        # 메타 없는 상품은 결과에서 제외
        assert len(results) == 1
        assert results[0].id == "p1"

    @pytest.mark.asyncio
    @patch("agents.alternative_agent.get_products_meta")
    @patch("agents.alternative_agent.match_alternatives")
    async def test_exclude_ids_passed_to_rpc(self, mock_match_alt, mock_get_metas):
        mock_match_alt.return_value = []
        await run_alternative("base-id", exclude_ids=["c1", "c2"], top_k=5)

        args = mock_match_alt.call_args.args
        # match_alternatives(base_product_id, top_k, exclude_ids)
        assert args[0] == "base-id"
        assert args[1] == 5
        assert args[2] == ["c1", "c2"]