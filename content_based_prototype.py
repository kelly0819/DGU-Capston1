from qdrant_utils import VectorDB

db = VectorDB()
FEATURES = ["matte", "glow", "long_lasting", "coverage_high",
            "lightweight", "drying", "cakey", "oxidation"]

FEATURE_LABEL_KR = {
    "matte": "매트 마감", "glow": "광채감", "long_lasting": "지속력 우수",
    "coverage_high": "고커버", "lightweight": "가벼운 사용감",
    "drying": "건조함(주의)", "cakey": "뭉침(주의)", "oxidation": "다크닝(주의)"
}

def content_based_recommend(user_id_int, top_n=5):
    me = db.get_by_id("user_vectors", user_id_int)
    results = db.cosine_search("product_vectors", me.vector, limit=top_n)
    
    print(f"\n[사용자 {user_id_int}에 대한 콘텐츠 기반 추천]")
    for p in results:
        # 추천 이유 추출: 사용자-상품 feature 일치도가 높은 Top 3
        p_full = db.get_by_id("product_vectors", p.id)
        matches = []
        for i, fname in enumerate(FEATURES):
            match_score = min(me.vector[i], p_full.vector[i])  # 둘 다 높을 때만 점수 ↑
            matches.append((fname, match_score))
        matches.sort(key=lambda x: -x[1]) # 점수 높은 순으로 정렬
        top_reasons = [FEATURE_LABEL_KR[m[0]] for m in matches[:3]] # 상위 3개만
        
        print(f"  - {p.payload.get('product_id')} ({p.payload.get('brand')}, 점수: {p.score:.3f})")
        print(f"    추천 이유: {', '.join(top_reasons)}")

content_based_recommend(user_id_int=1, top_n=5)