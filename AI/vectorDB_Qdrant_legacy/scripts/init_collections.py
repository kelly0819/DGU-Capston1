"""
Qdrant collection 초기 생성 스크립트.

사용법:
    docker compose exec api python scripts/init_collections.py
"""
import sys
sys.path.insert(0, "/code")

from qdrant_client.models import VectorParams, Distance
from vector_store import client, COLLECTION_CONFIG


# payload index 정의
PAYLOAD_INDEXES = {
    "user_vectors_base_makeup": ["user_id", "skin_type", "personal_color"],
    "user_vectors_skincare":    ["user_id", "skin_type", "personal_color"],
    "user_vectors_lip_eye":     ["user_id", "skin_type", "personal_color"],
    "product_vectors_base_makeup": ["product_id", "brand_id", "category_sub", "price"],
    "product_vectors_skincare":    ["product_id", "brand_id", "category_sub", "price"],
    "product_vectors_lip_eye":     ["product_id", "brand_id", "category_sub", "price"],
    "review_embeddings":  ["product_id"],
    "purchase_reasons":   ["user_id", "category"],
}


def main():
    for name, config in COLLECTION_CONFIG.items():
        if client.collection_exists(name):
            print(f"[skip] {name} already exists")
            continue
        
        client.create_collection(
            collection_name=name,
            vectors_config=VectorParams(
                size=config["size"],
                distance=config["distance"],
            ),
        )
        print(f"[created] {name}")
        
        # payload index 생성
        for field in PAYLOAD_INDEXES.get(name, []):
            if field == "price":
                schema = "integer"
            else:
                schema = "keyword"
            client.create_payload_index(
                collection_name=name,
                field_name=field,
                field_schema=schema,
            )
            print(f"  + index: {field} ({schema})")


if __name__ == "__main__":
    main()