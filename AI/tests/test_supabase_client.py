"""Supabase 클라이언트 싱글톤 + 연결 테스트."""
import pytest

from config import settings
from db.supabase_client import get_supabase


@pytest.mark.skipif(
    not settings.SUPABASE_URL or not settings.SUPABASE_SERVICE_KEY,
    reason="Supabase 환경변수 미설정",
)
class TestSupabaseClient: 
    # Supabase client가 싱글톤인지 테스트
    def test_singleton(self):
        client1 = get_supabase()
        client2 = get_supabase()
        assert client1 is client2

    # 실제 Supabase RPC 연결 되는지 테스트
    def test_rpc_connection(self):
        """Phase 1의 match_products RPC로 연결 검증."""
        client = get_supabase()
        result = client.rpc(
            "match_products",
            {
                "query_embedding": [0.1] * 1024,
                "match_category": None,
                "match_count": 1,
                "exclude_ids": [],
            },
        ).execute()
        assert result.data is not None