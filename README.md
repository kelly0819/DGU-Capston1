# Cosmetics Recommender — VectorDB Module

화장품 추천 AI 시스템의 **벡터 DB 모듈**.
사용자/상품/리뷰/구매 사유 벡터를 관리하고, 외부 팀(LLM, VLM, 아키텍처)이 사용할 검색 API를 제공한다.

## 구성

- **Postgres**: 원본 데이터 (users, product, review, product_feature, …)
- **Qdrant**: 벡터 검색 인덱스 (product_vectors_*, user_vectors_*, review_embeddings, purchase_reasons)
- **FastAPI**: 외부 팀과의 인터페이스. 두 DB를 감싸는 통합 API.

전체 구조는 `schema_design.md` 참고.

## 빠른 시작

```bash
# 1. 도커 띄우기
docker compose up -d

# 2. Qdrant collection 초기화 (한 번만)
docker compose exec api python scripts/init_collections.py

# 3. SQL → Qdrant 동기화
docker compose exec api python scripts/reindex_all.py

# 4. API 문서 확인
# 브라우저에서 http://localhost:8000/docs
```

## 주요 엔드포인트

| Method | Path | 용도 |
|--------|------|------|
| POST   | /products/upsert            | 상품 등록/갱신 |
| GET    | /users/{user_id}/vector     | 사용자 벡터 조회 |
| POST   | /search/similar-products    | 콘텐츠 기반 추천 |
| POST   | /search/similar-users       | 협업 필터링 |
| POST   | /reasons/retrieve           | RAG 검색 |

## 정지

```bash
docker compose down       # 컨테이너만 중지
docker compose down -v    # 데이터까지 삭제 (초기화)
```

## 팀 레포로 옮기기

현재는 개인 레포. 통합 시점에는 `app/` 폴더만 팀 레포로 옮기고,
환경 변수(`DATABASE_URL`, `QDRANT_URL`)는 팀 인프라 기준으로 재설정한다.