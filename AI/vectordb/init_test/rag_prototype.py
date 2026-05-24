from qdrant_utils import VectorDB
from sentence_transformers import SentenceTransformer

db = VectorDB()
model = SentenceTransformer("jhgan/ko-sroberta-multitask")

db.create_collection("purchase_reasons", size=768)

# 사용자 1의 구매 사유 누적 데이터
user1_reasons = [
    "지속력이 좋아서 자주 사용해요",
    "땀에도 안 지워져서 만족",
    "한 번 바르면 하루 종일 유지돼서 좋아요",
]
user2_reasons = [
    "촉촉하고 발림성이 좋아요",
    "수분감이 풍부해서 마음에 들어요",
]

points = []
pid = 1
for r in user1_reasons:
    points.append({
        "id": pid,
        "vector": model.encode(r).tolist(),
        "payload": {"user_id": "u001", "category": "base_makeup", "text": r}
    })
    pid += 1
for r in user2_reasons:
    points.append({
        "id": pid,
        "vector": model.encode(r).tolist(),
        "payload": {"user_id": "u002", "category": "base_makeup", "text": r}
    })
    pid += 1
db.upsert_batch("purchase_reasons", points)

# 검색: 사용자 1의 과거 사유 패턴 추출
def retrieve_user_reasons(user_id, category, query_text, top_k=5):
    q_vec = model.encode(query_text).tolist()
    results = db.filtered_search(
        "purchase_reasons",
        q_vec,
        filters={"user_id": user_id, "category": category},
        limit=top_k
    )
    print(f"\n[{user_id}의 '{query_text}' 관련 과거 사유]")
    for r in results:
        print(f"  - 점수: {r.score:.3f} / {r.payload['text']}")
    return results

# 본 프로젝트에서는 이 결과를 LLM에 넘겨 feature 벡터로 변환할 예정
# (현재는 LLM 담당자와 협업 전이므로 mock)
def mock_llm_to_feature_vector(retrieved):
    # 실제로는 LLM이 "long_lasting=0.9, drying=0.2" 같은 결과를 반환
    return {"long_lasting": 0.9, "matte": 0.7, "drying": 0.2}

retrieved = retrieve_user_reasons("u001", "base_makeup", "오래 가는 제품 추천해주세요")
features = mock_llm_to_feature_vector(retrieved)
print(f"\n추출된 사용자 의도 feature: {features}")