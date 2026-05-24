from sqlalchemy.orm import Session
from qdrant_client.models import PointStruct

from models import Brand, Product, ProductFeature
from schemas import ProductUpsertRequest, ProductUpsertResponse
from vector_store import client as qdrant, feature_dict_to_vector, get_vector_collection_name


def upsert_product(body: ProductUpsertRequest, db: Session) -> ProductUpsertResponse:
    """상품 등록/갱신. SQL과 Qdrant에 동시 반영."""

    # 1. 브랜드 찾거나 생성
    brand = db.query(Brand).filter(Brand.name == body.brand_name).first()
    if brand is None:
        brand = Brand(name=body.brand_name)
        db.add(brand)
        db.flush()  # brand_id를 받아오기 위해 (commit 전에 ID 할당)

    # 2. 기존 상품인지 확인
    created = False
    if body.product_id is not None:
        product = db.query(Product).filter(Product.product_id == body.product_id).first()
        if product is None:
            raise ValueError(f"product_id {body.product_id} not found")
    else:
        product = Product()
        created = True

    # 3. 상품 필드 업데이트
    product.name           = body.name
    product.brand_id       = brand.brand_id
    product.category       = body.category_main
    product.image_url      = body.image_url
    product.price          = body.price
    product.rating         = body.rating
    product.review_summary = body.review_summary

    if created:
        db.add(product)
    db.flush()  # product_id 확보

    # 4. product_feature 저장 (있으면 갱신, 없으면 신규)
    feature = db.query(ProductFeature).filter(
        ProductFeature.product_id == product.product_id
    ).first()
    if feature is None:
        feature = ProductFeature(product_id=product.product_id)
        db.add(feature)

    feature.type         = body.category_sub
    feature.skin_type    = body.skin_type
    feature.feature_json = body.feature_json

    # 5. Qdrant에 벡터 upsert (feature_json을 배열로 변환)
    vector = feature_dict_to_vector(body.category_main, body.feature_json)
    collection_name = get_vector_collection_name(body.category_main, kind="product")

    point = PointStruct(
        id=product.product_id,
        vector=vector,
        payload={
            "product_id":    product.product_id,
            "name":          product.name,
            "brand_id":      product.brand_id,
            "brand_name":    body.brand_name,
            "category_sub":  body.category_sub,
            "skin_type":     body.skin_type,
            "price":         product.price,
        },
    )
    qdrant.upsert(collection_name=collection_name, points=[point])

    # 6. 모두 성공했으면 commit
    db.commit()

    return ProductUpsertResponse(product_id=product.product_id, created=created)