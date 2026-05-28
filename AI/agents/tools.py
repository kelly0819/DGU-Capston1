from langchain_core.tools import tool


# ── 시나리오 1, 2 도구 ─────────────────────────────────────────────────────

@tool
def gemini_price_review(product_name: str, brand: str) -> dict:
    """화장품의 최저가와 리뷰를 웹 검색으로 조회합니다.
    올리브영, 쿠팡, 네이버쇼핑에서 최저가를 찾고 실제 구매자 리뷰를 요약합니다."""
    # agents/product_agent/gemini_enricher.py 연결 예정
    return {}


@tool
def search_rag(query: str, category: str) -> list:
    """벡터 DB에서 쿼리와 유사한 상품을 검색합니다.
    사용자 니즈 텍스트와 카테고리를 기반으로 관련 상품 목록을 반환합니다."""
    # db/vector_search.py 연결 예정
    return []


@tool
def compute_matching_score(product_id: str, user_id: str) -> dict:
    """특정 상품과 사용자 간의 매칭 점수를 계산합니다.
    예산 적합도, 리뷰 점수, 개인화 점수를 종합한 최종 점수를 반환합니다."""
    # agents/score_agent/ 연결 예정
    return {"product_id": product_id, "score": 0.0, "breakdown": {}}


@tool
def collab_filter(category: str, user_id: str) -> list:
    """협업 필터링으로 유사한 취향의 사용자들이 선택한 상품을 추천합니다.
    DB 데이터가 충분할 때 후보 상품 목록을 반환합니다."""
    # agents/collaborative_agent/ 연결 예정
    return []


# ── 시나리오 3 전용 도구 (Cold Start 해결) ────────────────────────────────

@tool
def gemini_web_search(category: str, budget: int, conditions: str) -> list:
    """올리브영, 쿠팡, 네이버쇼핑을 탐색해 조건에 맞는 상품 후보를 수집합니다.
    협업 필터링 데이터가 부족한 Cold Start 상황에서 사용합니다."""
    # Gemini Google Search grounding 연결 예정
    return []


@tool
def save_feature_vector(products: list) -> bool:
    """수집된 상품의 feature 벡터를 생성하고 DB에 저장합니다.
    gemini_web_search로 수집한 상품을 협업 필터링 후보풀로 등록할 때 사용합니다."""
    # services/embedding_service.py 연결 예정
    return True


ALL_TOOLS = [
    gemini_price_review,
    search_rag,
    compute_matching_score,
    collab_filter,
    gemini_web_search,
    save_feature_vector,
]
