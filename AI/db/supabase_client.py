"""
Supabase 클라이언트 싱글톤.

모든 DB 접근은 이 모듈의 get_supabase()를 거쳐야 한다.
직접 create_client() 호출 금지.

service_role 키를 사용하므로 RLS를 우회할 수 있다.
client-side 코드(브라우저·앱)에서는 절대 사용하지 말 것.
"""
from functools import lru_cache

from supabase import Client, create_client

from config import settings


@lru_cache(maxsize=1)
def get_supabase() -> Client:
    """Supabase service_role 클라이언트 싱글톤."""
    return create_client(
        settings.SUPABASE_URL,
        settings.SUPABASE_SERVICE_KEY,
    )