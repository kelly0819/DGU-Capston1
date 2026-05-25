"""
SQL -> Qdrant 전체 재동기화.

사용법:
    docker compose exec api python scripts/reindex_all.py
"""
import sys
sys.path.insert(0, "/code")

from qdrant_client.models import PointStruct
from sqlalchemy.orm import sessionmaker

from db import engine
from models import Product, ProductFeature, Review, UserContextRag, Brand
from vector_store import (
    client as qdrant,
    feature_dict_to_vector,
    get_vector_collection_name,
)
from embedding import embed_texts
from services.user_service import compute_user_vector, upsert_user_vector


SessionLocal = sessionmaker(bind=engine)
BATCH_SIZE = 64


def reindex_products(db):
    print("=== Reindexing products ===")
    features = db.query(ProductFeature).all()
    
    by_category = {}
    for f in features:
        product = db.query(Product).filter(Product.product_id == f.product_id).first()
        if product is None:
            continue
        by_category.setdefault(product.category, []).append((f, product))
    
    for category, items in by_category.items():
        points = []
        for feature, product in items:
            brand = db.query(Brand).filter(Brand.brand_id == product.brand_id).first()
            vector = feature_dict_to_vector(category, feature.feature_json)
            points.append(PointStruct(
                id=product.product_id,
                vector=vector,
                payload={
                    "product_id":   product.product_id,
                    "name":         product.name,
                    "brand_id":     product.brand_id,
                    "brand_name":   brand.name if brand else "",
                    "category_sub": feature.type,
                    "skin_type":    feature.skin_type,
                    "price":        product.price,
                },
            ))
        
        collection = get_vector_collection_name(category, kind="product")
        qdrant.upsert(collection_name=collection, points=points)
        print(f"  [{collection}] {len(points)} points")


def reindex_reviews(db):
    print("=== Reindexing reviews ===")
    reviews = db.query(Review).all()
    
    for i in range(0, len(reviews), BATCH_SIZE):
        batch = reviews[i:i+BATCH_SIZE]
        texts = [r.content for r in batch]
        vectors = embed_texts(texts)
        
        points = [
            PointStruct(
                id=r.review_id,
                vector=v,
                payload={
                    "review_id":  r.review_id,
                    "product_id": r.product_id,
                    "rating":     r.rating,
                    "source":     r.source,
                    "text":       r.content[:200],
                },
            )
            for r, v in zip(batch, vectors)
        ]
        qdrant.upsert(collection_name="review_embeddings", points=points)
        print(f"  batch {i // BATCH_SIZE + 1}: {len(points)} points")


def reindex_reasons(db):
    print("=== Reindexing purchase reasons ===")
    reasons = db.query(UserContextRag).all()
    
    for i in range(0, len(reasons), BATCH_SIZE):
        batch = reasons[i:i+BATCH_SIZE]
        texts = [r.content for r in batch]
        vectors = embed_texts(texts)
        
        points = [
            PointStruct(
                id=r.rag_id,
                vector=v,
                payload={
                    "rag_id":   r.rag_id,
                    "user_id":  r.user_id,
                    "category": r.category,
                    "text":     r.content,
                },
            )
            for r, v in zip(batch, vectors)
        ]
        qdrant.upsert(collection_name="purchase_reasons", points=points)
        print(f"  batch {i // BATCH_SIZE + 1}: {len(points)} points")


def reindex_user_vectors(db):
    print("=== Computing user vectors ===")
    from models import User
    users = db.query(User).all()
    
    for user in users:
        for category in ["base_makeup", "skincare", "lip_eye"]:
            vector = compute_user_vector(user.user_id, category, db)
            upsert_user_vector(user.user_id, category, vector)
        print(f"  user_id={user.user_id} done")


def main():
    db = SessionLocal()
    try:
        reindex_products(db)
        reindex_reviews(db)
        reindex_reasons(db)
        reindex_user_vectors(db)
        print("\n✅ All reindexing complete")
    finally:
        db.close()


if __name__ == "__main__":
    main()