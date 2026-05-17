from qdrant_client import QdrantClient
from qdrant_client.models import (
    Distance, VectorParams, PointStruct,
    Filter, FieldCondition, MatchValue
)

class VectorDB:
    def __init__(self, host="localhost", port=6333):
        self.client = QdrantClient(host=host, port=port)

    def create_collection(self, name, size, distance=Distance.COSINE):
        """Collection 생성 """
        if self.client.collection_exists(name):
            self.client.delete_collection(name)
        self.client.create_collection(
            collection_name=name,
            vectors_config=VectorParams(size=size, distance=distance)
        )
        print(f"[CREATED] {name} (size={size})")
    
    def upsert_one(self, collection, point_id, vector, payload=None):
        """단건 삽입/수정"""
        self.client.upsert(
            collection_name=collection,
            points=[PointStruct(id=point_id, vector=vector, payload=payload or {})]
        )
    
    def upsert_batch(self, collection, items):
        """배치 삽입. items=[{'id':, 'vector':, 'payload':}, ...]"""
        points = [
            PointStruct(id=i['id'], vector=i['vector'], payload=i.get('payload', {}))
            for i in items
        ]
        self.client.upsert(collection_name=collection, points=points)
        print(f"[UPSERTED] {len(points)} points into {collection}")
    
    def cosine_search(self, collection, query_vector, limit=10):
        """기본 코사인 유사도 검색"""
        return self.client.query_points(
            collection_name=collection,
            query=query_vector,
            limit=limit
        ).points
    
    def filtered_search(self, collection, query_vector, filters: dict, limit=10):
        """payload 필터 검색. filters={'category': 'base_makeup'} 형태"""
        conditions = [
            FieldCondition(key=k, match=MatchValue(value=v))
            for k, v in filters.items()
        ]
        return self.client.query_points(
            collection_name=collection,
            query=query_vector,
            query_filter=Filter(must=conditions),
            limit=limit
        ).points
    
    def get_by_id(self, collection, point_id):
        """ID로 단건 조회"""
        result = self.client.retrieve(
            collection_name=collection,
            ids=[point_id],
            with_vectors=True
        )
        return result[0] if result else None