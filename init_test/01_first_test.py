from qdrant_client import QdrantClient
from qdrant_client.models import Distance, VectorParams, PointStruct

# Qdrant에 연결
client = QdrantClient(host="localhost", port=6333)

collection_name = "test_collection"

# 1. Collection 만들기
if client.collection_exists(collection_name):
    client.delete_collection(collection_name)

client.create_collection(
    collection_name=collection_name,
    vectors_config=VectorParams(size=4, distance=Distance.COSINE),
)
print("Collection 생성 완료")

# 2. Point 3개 삽입
client.upsert(
    collection_name=collection_name,
    points=[
        PointStruct(id=1, vector=[0.1, 0.2, 0.3, 0.4], payload={"name": "쿠션 A", "brand": "HERA"}),
        PointStruct(id=2, vector=[0.15, 0.25, 0.35, 0.45], payload={"name": "쿠션 B", "brand": "LANEIGE"}),
        PointStruct(id=3, vector=[0.9, 0.8, 0.7, 0.6], payload={"name": "립스틱 A", "brand": "HERA"}),
    ],
)
print("데이터 삽입 완료")

# 3. 검색: query_points 사용 (.points로 결과 리스트 접근)
response = client.query_points(
    collection_name=collection_name,
    query=[0.1, 0.2, 0.3, 0.4],
    limit=3,
)
for r in response.points:
    print(f"ID: {r.id}, 점수: {r.score:.4f}, 정보: {r.payload}")