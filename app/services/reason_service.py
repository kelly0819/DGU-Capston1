from sqlalchemy.orm import Session
from qdrant_client.models import PointStruct, Filter, FieldCondition, MatchValue

from models import UserContextRag, User
from schemas import (
    ReasonAddRequest, ReasonAddResponse,
    ReasonRetrieveRequest, ReasonRetrieveResponse, ReasonResult,
)
from qdrant_client import client as qdrant
from embedding import embed_text


REASON_COLLECTION = "purchase_reasons"


def add_reason(user_id: int, body: ReasonAddRequest, db: Session) -> ReasonAddResponse:
    """구매 사유를 SQL과 Qdrant에 동시 저장."""
    # 사용자 존재 확인
    user = db.query(User).filter(User.user_id == user_id).first()
    if user is None:
        raise ValueError(f"user_id {user_id} not found")

    # 1. SQL에 원문 저장 (rag_id 생성됨)
    rag_row = UserContextRag(
        user_id=user_id,
        content=body.content,
        category=body.category,
    )
    db.add(rag_row)
    db.flush()

    # 2. 임베딩 + Qdrant 저장 (rag_id 그대로 사용)
    vector = embed_text(body.content)
    point = PointStruct(
        id=rag_row.rag_id,
        vector=vector,
        payload={
            "rag_id":   rag_row.rag_id,
            "user_id":  user_id,
            "category": body.category,
            "text":     body.content,
        },
    )
    qdrant.upsert(collection_name=REASON_COLLECTION, points=[point])
    
    db.commit()
    return ReasonAddResponse(rag_id=rag_row.rag_id)


def retrieve_reasons(body: ReasonRetrieveRequest, db: Session) -> ReasonRetrieveResponse:
    """동일 user_id + 동일 category 내에서 유사 구매 사유 검색."""
    query_vector = embed_text(body.query_text)
    
    # 필터: user_id와 category 모두 일치
    query_filter = Filter(
        must=[
            FieldCondition(key="user_id",  match=MatchValue(value=body.user_id)),
            FieldCondition(key="category", match=MatchValue(value=body.category)),
        ]
    )
    
    response = qdrant.query_points(
        collection_name=REASON_COLLECTION,
        query=query_vector,
        query_filter=query_filter,
        limit=body.top_k,
    )
    
    # rag_id들로 SQL에서 메타 정보(created_at) 가져오기
    rag_ids = [p.id for p in response.points]
    rag_rows = {
        row.rag_id: row
        for row in db.query(UserContextRag).filter(UserContextRag.rag_id.in_(rag_ids)).all()
    }
    
    results = []
    for p in response.points:
        row = rag_rows.get(p.id)
        if row is None:
            continue
        results.append(ReasonResult(
            rag_id=p.id,
            text=row.content,
            score=p.score,
            created_at=row.created_at,
        ))
    
    return ReasonRetrieveResponse(reasons=results)