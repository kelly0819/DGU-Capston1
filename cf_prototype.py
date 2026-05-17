from qdrant_utils import VectorDB

db = VectorDB()

def collaborative_filtering(user_id_int, top_k_users=5):
    # 1. 내 벡터 가져오기
    me = db.get_by_id("user_vectors", user_id_int)
    if not me:
        return []
    my_vector = me.vector
    my_persona = me.payload.get("persona")
    
    # 2. 비슷한 사용자 Top-K 검색 (나 자신 제외 위해 +1)
    similar = db.cosine_search("user_vectors", my_vector, limit=top_k_users+1)
    similar = [s for s in similar if s.id != user_id_int][:top_k_users]
    
    print(f"\n[사용자 {user_id_int} (성향: {my_persona})와 비슷한 사용자]")
    for s in similar:
        print(f"  - 사용자 {s.id} (성향: {s.payload.get('persona')}, 유사도: {s.score:.3f})")
    
    # 3. 비슷한 사용자들의 평균 벡터로 상품 검색
    import numpy as np
    avg_vec = np.mean([db.get_by_id("user_vectors", s.id).vector for s in similar], axis=0).tolist()
    
    candidate_products = db.cosine_search("product_vectors", avg_vec, limit=10)
    print(f"\n[추천 후보 상품 Top 10]")
    for p in candidate_products:
        print(f"  - {p.payload.get('product_id')} ({p.payload.get('brand')}, 타입: {p.payload.get('type')}, 점수: {p.score:.3f})")
    return candidate_products

# 테스트
collaborative_filtering(user_id_int=1, top_k_users=5)