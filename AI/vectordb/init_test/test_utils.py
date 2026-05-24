from qdrant_utils import VectorDB

db = VectorDB()

# Collection 만들기
db.create_collection("test_utils", size=4)

# 데이터 저장
db.upsert_batch("test_utils", [
    {"id": 1, "vector": [0.1, 0.2, 0.3, 0.4], "payload": {"name": "쿠션 A"}},
    {"id": 2, "vector": [0.9, 0.8, 0.7, 0.6], "payload": {"name": "립스틱 A"}},
])

# 검색
results = db.cosine_search("test_utils", [0.1, 0.2, 0.3, 0.4], limit=2)
for r in results:
    print(f"ID: {r.id}, 점수: {r.score:.4f}, 정보: {r.payload}")

# ID로 조회
point = db.get_by_id("test_utils", 1)
print(f"\nID 1번 데이터: {point.payload}")