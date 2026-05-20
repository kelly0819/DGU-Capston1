from sqlalchemy.orm import Session
from sqlalchemy import and_
from qdrant_client.models import PointStruct

from models import User, UserProfile, UserProduct, ProductFeature, Product
from schemas import UserVectorResponse, CategoryMain
from qdrant_client import (
    client as qdrant,
    feature_dict_to_vector,
    get_vector_collection_name,
    FEATURE_KEYS,
)


def compute_user_vector(user_id: int, category: CategoryMain, db: Session) -> list[float]:
    """사용자가 사용 중인 같은 카테고리 상품들의 feature 평균으로 user vector 계산."""
    dim = len(FEATURE_KEYS[category])
    
    # 사용 중인 상품들의 feature 가져오기 (데이터가 충분히 쌓이면 (사용자당 100개+ 평가) 그때 가서 "rating < 3 제외" 같은 조건을 더하는 식으로 발전시키기)
    rows = (
        db.query(ProductFeature, UserProduct)
        .join(Product, Product.product_id == ProductFeature.product_id)
        .join(UserProduct, UserProduct.product_id == Product.product_id)
        .filter(and_(
            UserProduct.user_id == user_id,
            Product.category == category,
            UserProduct.usage_type == "사용 중",
        ))
        .all()
    )

    if not rows:
        # 데이터 없으면 중립값(0.5) 벡터로 시작
        return [0.5] * dim

    # 각 상품 feature를 벡터로 바꿔서 평균 (rating으로 가중평균)
    weighted_sum = [0.0] * dim
    weight_total = 0.0

    for feature, user_product in rows:
        vec = feature_dict_to_vector(category, feature.feature_json)
        weight = user_product.rating if user_product.rating else 3.0
        for i in range(dim):
            weighted_sum[i] += vec[i] * weight
        weight_total += weight

    return [v / weight_total for v in weighted_sum]


def upsert_user_vector(user_id: int, category: CategoryMain, vector: list[float]):
    """계산된 user vector를 Qdrant에 저장."""
    collection_name = get_vector_collection_name(category, kind="user")
    point = PointStruct(
        id=user_id,
        vector=vector,
        payload={"user_id": user_id},
    )
    qdrant.upsert(collection_name=collection_name, points=[point])


def get_user_vector(user_id: int, category: CategoryMain, db: Session) -> UserVectorResponse:
    """API 응답용. 매번 다시 계산해서 Qdrant에도 업데이트."""
    # 사용자 존재 확인
    user = db.query(User).filter(User.user_id == user_id).first()
    if user is None:
        raise ValueError(f"user_id {user_id} not found")
    
    profile = db.query(UserProfile).filter(UserProfile.user_id == user_id).first()
    
    # 동적 계산
    vector = compute_user_vector(user_id, category, db)
    
    # Qdrant에도 반영 (협업 필터링용)
    upsert_user_vector(user_id, category, vector)
    
    return UserVectorResponse(
        user_id=user_id,
        category=category,
        vector=vector,
        skin_type=profile.skin_type if profile else None,
        personal_color=profile.personal_color if profile else None,
    )