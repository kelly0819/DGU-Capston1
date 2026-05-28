"""
vector_search RPC wrapper 통합 테스트.

실제 Supabase 연결이 필요하며, RPC가 에러 없이 정상 동작하는지 검증.
데이터가 없는 환경에서도 빈 리스트를 반환해야 한다.
"""

import pytest
from config import settings

from db.vector_search import (
    match_alternatives,
    match_products,
    match_reviews,
    match_user_contexts,
)

DUMMY_UUID = "00000000-0000-0000-0000-000000000000"


@pytest.mark.skipif(
    not settings.SUPABASE_URL or not settings.SUPABASE_SERVICE_KEY,
    reason="Supabase 환경변수 미설정",
)
class TestVectorSearch:
    def _dummy_vector(self):
        return [0.1] * 1024

    def test_match_products_returns_list(self):
        result = match_products(self._dummy_vector(), match_count=5)
        assert isinstance(result, list)

    def test_match_products_with_category(self):
        result = match_products(
            self._dummy_vector(), category="base", match_count=5
        )
        assert isinstance(result, list)

    def test_match_products_with_exclude_ids(self):
        result = match_products(
            self._dummy_vector(),
            match_count=5,
            exclude_ids=[DUMMY_UUID],
        )
        assert isinstance(result, list)

    def test_match_alternatives_nonexistent_base(self):
        # 존재하지 않는 base_product_id면 빈 리스트
        result = match_alternatives(DUMMY_UUID, match_count=5)
        assert result == []

    def test_match_reviews_returns_list(self):
        result = match_reviews(self._dummy_vector(), DUMMY_UUID, match_count=5)
        assert isinstance(result, list)

    def test_match_user_contexts_returns_list(self):
        result = match_user_contexts(
            DUMMY_UUID, self._dummy_vector(), match_count=5
        )
        assert isinstance(result, list)