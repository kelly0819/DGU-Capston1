from sqlalchemy.orm import Session
from qdrant_client.models import Filter, FieldCondition, MatchValue, Range

from models import Product, Brand
from schemas import (
    SimilarProductsRequest, SimilarProductsResponse, ProductSearchResult,
    SimilarUsersRequest, SimilarUsersResponse, UserSearchResult,
)
from app.vector_store import client as qdrant, get_vector_collection_name


def search_similar_products(body: SimilarProductsRequest, db: Session) -> SimilarProductsResponse:
    """사용자 벡터와 비슷한 상품 검색 (콘텐츠 기반)."""
    collection_name = get_vector_collection_name(body.category, kind="product")
    
    # 필터 조건 (가격 상한 등)
    filter_conditions = []
    if body.price_max is not None:
        filter_conditions.append(
            FieldCondition(key="price", range=Range(lte=body.price_max))
        )
    query_filter = Filter(must=filter_conditions) if filter_conditions else None
    
    response = qdrant.query_points(
        collection_name=collection_name,
        query=body.user_vector,
        query_filter=query_filter,
        limit=body.top_k,
    )
    
    # SQL에서 부가 정보 가져오기
    product_ids = [p.id for p in response.points]
    products = {
        p.product_id: p
        for p in db.query(Product).filter(Product.product_id.in_(product_ids)).all()
    }
    brands = {
        b.brand_id: b
        for b in db.query(Brand).filter(
            Brand.brand_id.in_([p.brand_id for p in products.values() if p.brand_id])
        ).all()
    }
    
    results = []
    for p in response.points:
        product = products.get(p.id)
        if product is None:
            continue
        brand_name = brands[product.brand_id].name if product.brand_id else ""
        results.append(ProductSearchResult(
            product_id=p.id,
            score=p.score,
            name=product.name,
            brand_name=brand_name,
            price=product.price,
        ))
    
    return SimilarProductsResponse(results=results)


def search_similar_users(body: SimilarUsersRequest, db: Session) -> SimilarUsersResponse:
    """주어진 user_id와 취향이 비슷한 사용자 검색 (협업 필터링)."""
    collection_name = get_vector_collection_name(body.category, kind="user")
    
    # 먼저 본인 벡터를 Qdrant에서 가져옴
    own_points = qdrant.retrieve(
        collection_name=collection_name,
        ids=[body.user_id],
        with_vectors=True,
    )
    if not own_points:
        raise ValueError(f"user_id {body.user_id} has no vector in {collection_name}")
    
    own_vector = own_points[0].vector
    
    # 자기 자신은 제외하고 검색
    response = qdrant.query_points(
        collection_name=collection_name,
        query=own_vector,
        limit=body.top_k + 1,
    )
    
    results = [
        UserSearchResult(user_id=p.id, score=p.score)
        for p in response.points if p.id != body.user_id
    ][:body.top_k]
    
    return SimilarUsersResponse(similar_users=results)