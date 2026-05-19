from fastapi import FastAPI, Depends, HTTPException, Path, Query
from sqlalchemy.orm import Session

from db import get_db
from schemas import (
    ProductUpsertRequest, ProductUpsertResponse,
    UserVectorResponse,
    SimilarProductsRequest, SimilarProductsResponse,
    SimilarUsersRequest, SimilarUsersResponse,
    ReasonRetrieveRequest, ReasonRetrieveResponse,
    ReasonAddRequest, ReasonAddResponse,
    CategoryMain,
)


app = FastAPI(
    title="Cosmetics VectorDB API",
    description="화장품 추천 시스템의 벡터 검색·저장 모듈",
    version="0.1.0",
)


# =========================================
# 헬스체크
# =========================================

@app.get("/health")
def health_check():
    return {"status": "ok"}


# =========================================
# 상품 등록 (VLM/Agent2 팀 -> 이곳)
# =========================================

@app.post("/products/upsert", response_model=ProductUpsertResponse)
def upsert_product(
    body: ProductUpsertRequest,
    db: Session = Depends(get_db),
):
    # 실제 로직은 product_service.py에 구현할 예정
    # 일단은 자리만 잡아두고 NotImplementedError 발생
    raise HTTPException(status_code=501, detail="구현 예정")


# =========================================
# 사용자 벡터 조회 (Agent3 -> 이곳)
# =========================================

@app.get("/users/{user_id}/vector", response_model=UserVectorResponse)
def get_user_vector(
    user_id: int = Path(..., description="조회할 사용자 ID"),
    category: CategoryMain = Query(..., description="카테고리"),
    db: Session = Depends(get_db),
):
    raise HTTPException(status_code=501, detail="구현 예정")


# =========================================
# 콘텐츠 기반 추천 (Agent3 -> 이곳)
# =========================================

@app.post("/search/similar-products", response_model=SimilarProductsResponse)
def search_similar_products(
    body: SimilarProductsRequest,
    db: Session = Depends(get_db),
):
    raise HTTPException(status_code=501, detail="구현 예정")


# =========================================
# 협업 필터링 (Agent3 -> 이곳)
# =========================================

@app.post("/search/similar-users", response_model=SimilarUsersResponse)
def search_similar_users(
    body: SimilarUsersRequest,
    db: Session = Depends(get_db),
):
    raise HTTPException(status_code=501, detail="구현 예정")


# =========================================
# RAG 구매 사유 검색 (Agent3 -> 이곳)
# =========================================

@app.post("/reasons/retrieve", response_model=ReasonRetrieveResponse)
def retrieve_reasons(
    body: ReasonRetrieveRequest,
    db: Session = Depends(get_db),
):
    raise HTTPException(status_code=501, detail="구현 예정")


# =========================================
# 구매 사유 추가 (사용자 -> 이곳)
# =========================================

@app.post("/users/{user_id}/reasons", response_model=ReasonAddResponse)
def add_reason(
    user_id: int,
    body: ReasonAddRequest,
    db: Session = Depends(get_db),
):
    raise HTTPException(status_code=501, detail="구현 예정")