from sqlalchemy.orm import Session
from qdrant_client.models import PointStruct

from models import Review
from app.vector_store import client as qdrant
from embedding import embed_text


def index_review(review_id: int, db: Session):
    """SQL의 review를 임베딩해서 Qdrant에 저장."""
    review = db.query(Review).filter(Review.review_id == review_id).first()
    if review is None:
        raise ValueError(f"review_id {review_id} not found")
    
    vector = embed_text(review.content)
    
    point = PointStruct(
        id=review_id,
        vector=vector,
        payload={
            "review_id":  review_id,
            "product_id": review.product_id,
            "rating":     review.rating,
            "source":     review.source,
            "text":       review.content[:200],
        },
    )
    qdrant.upsert(collection_name="review_embeddings", points=[point])