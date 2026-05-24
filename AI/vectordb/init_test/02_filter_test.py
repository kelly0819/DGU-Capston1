from qdrant_client import QdrantClient
from qdrant_client.models import Filter, FieldCondition, MatchValue

client = QdrantClient(host="localhost", port=6333)

# HERA 브랜드만 검색
response = client.query_points(
    collection_name="test_collection",
    query=[0.1, 0.2, 0.3, 0.4],
    query_filter=Filter(
        must=[FieldCondition(key="brand", match=MatchValue(value="HERA"))]
    ),
    limit=10,
)
for r in response.points:
    print(f"ID: {r.id}, 점수: {r.score:.4f}, 정보: {r.payload}")