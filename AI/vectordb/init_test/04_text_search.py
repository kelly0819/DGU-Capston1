from qdrant_client import QdrantClient
from qdrant_client.models import Distance, VectorParams, PointStruct
from sentence_transformers import SentenceTransformer

client = QdrantClient(host="localhost", port=6333)
model = SentenceTransformer("jhgan/ko-sroberta-multitask")  # 가장 좋았던 모델

collection_name = "purchase_reasons"

# 임베딩 차원에 맞춰 Collection 재생성
if client.collection_exists(collection_name):
    client.delete_collection(collection_name)

client.create_collection(
    collection_name=collection_name,
    vectors_config=VectorParams(size=768, distance=Distance.COSINE),
)

# 가짜 구매 사유 데이터
reasons = [
    {"id": 1, "text": "지속력이 좋아서 구매", "category": "base_makeup"},
    {"id": 2, "text": "촉촉해서 마음에 들어요", "category": "base_makeup"},
    {"id": 3, "text": "트러블 없이 잘 맞아요", "category": "skincare"},
    {"id": 4, "text": "발색이 또렷해서 만족", "category": "lip_makeup"},
    {"id": 5, "text": "오래 유지되어 좋음", "category": "base_makeup"},
]

# 임베딩 후 저장
points = []
for r in reasons:
    vec = model.encode(r["text"]).tolist()
    points.append(PointStruct(
        id=r["id"],
        vector=vec,
        payload={"text": r["text"], "category": r["category"]}
    ))
client.upsert(collection_name=collection_name, points=points)

# 검색: "오래 가는 제품" → 어떤 사유와 가장 유사한가?
query_text = "오래 가는 제품을 원해요"
query_vec = model.encode(query_text).tolist()

response = client.query_points(
    collection_name=collection_name,
    query=query_vec,
    limit=3,
)
print(f"\n질문: '{query_text}'\n")
for r in response.points:
    print(f"점수: {r.score:.4f} / {r.payload['text']} [{r.payload['category']}]")