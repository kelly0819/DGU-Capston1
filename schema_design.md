# Vector DB Schema 설계

> 멀티 에이전트 기반 쇼핑 보조 서비스 (화장품 카테고리)
> Vector DB / 트렌드 분석 담당
> 작성일: 2026-05-12

---

## 0. 사전 결정 사항

### 0-1. 임베딩 모델

| 항목 | 결정 |
|------|------|
| 모델명 | `jhgan/ko-sroberta-multitask` |
| 임베딩 차원 | 768 |
| 거리 측정 | Cosine Similarity |
| 선정 이유 | 한국어 의미 구분 능력 우수 (유사쌍-다른쌍 갭 0.31), 임베딩 속도 빠름 (0.04초), bge-m3 대비 차원이 작아 저장·검색 효율적 |

### 0-2. Collection 분리 전략

카테고리(베이스/스킨케어/립·아이)별로 **feature 차원이 다르기 때문에**, `user_vectors`와 `product_vectors`는 카테고리별로 별도 Collection으로 분리한다.

| Collection명 | 차원 | 용도 |
|--------------|------|------|
| `user_vectors_base_makeup` | 8 | 베이스 메이크업 사용자 벡터 |
| `user_vectors_skincare` | 5 | 스킨케어 사용자 벡터 |
| `user_vectors_lip_eye` | 4 | 립·아이 사용자 벡터 |
| `product_vectors_base_makeup` | 8 | 베이스 메이크업 상품 벡터 |
| `product_vectors_skincare` | 5 | 스킨케어 상품 벡터 |
| `product_vectors_lip_eye` | 4 | 립·아이 상품 벡터 |
| `purchase_reasons` | 768 | 구매 사유 텍스트 임베딩 (통합) |

`purchase_reasons`만 텍스트 임베딩이므로 카테고리에 무관하게 차원이 같아 단일 Collection으로 통합 운영. 카테고리 구분은 payload 필터로 처리.

---

## 1. 카테고리별 Feature 정의

본 프로젝트 종설 기획에 정의된 feature 목록을 그대로 채택.

### 1-1. 베이스 메이크업 (base_makeup) — 8차원

| 인덱스 | Feature | 한국어 의미 | 부정 feature 여부 |
|--------|---------|-------------|-------------------|
| 0 | `matte` | 매트 마감 | - |
| 1 | `glow` | 광채감 | - |
| 2 | `long_lasting` | 지속력 우수 | - |
| 3 | `coverage_high` | 고커버 | - |
| 4 | `lightweight` | 가벼운 사용감 | - |
| 5 | `drying` | 건조함 | ⚠️ 부정 |
| 6 | `cakey` | 뭉침 | ⚠️ 부정 |
| 7 | `oxidation` | 다크닝 | ⚠️ 부정 |

### 1-2. 스킨케어 (skincare) — 5차원

| 인덱스 | Feature | 한국어 의미 | 부정 feature 여부 |
|--------|---------|-------------|-------------------|
| 0 | `hydrating` | 수분감 | - |
| 1 | `soothing` | 진정 효과 | - |
| 2 | `non_sticky` | 끈적임 없음 | - |
| 3 | `fast_absorbing` | 빠른 흡수 | - |
| 4 | `irritation` | 자극 발생 | ⚠️ 부정 |

### 1-3. 립·아이 메이크업 (lip_eye) — 4차원

| 인덱스 | Feature | 한국어 의미 | 부정 feature 여부 |
|--------|---------|-------------|-------------------|
| 0 | `pigmentation` | 발색력 | - |
| 1 | `long_lasting` | 지속력 우수 | - |
| 2 | `smudge_proof` | 번짐 방지 | - |
| 3 | `drying` | 입술 건조함 | ⚠️ 부정 |

### 1-4. Feature 점수 규칙

- 모든 값은 `0.0 ~ 1.0` 범위로 정규화
- 긍정 feature: 리뷰/사유에서 자주 언급될수록 ↑
- 부정 feature: 리뷰/사유에서 자주 언급될수록 ↑ (점수가 높을수록 부정적 특성이 강함)
- 점수 산출 기준:
  - 긍정 다수 → 0.7 ~ 1.0
  - 보통 → 0.4 ~ 0.6
  - 부정 다수 또는 미언급 → 0.0 ~ 0.3

---

## 2. Collection 1: user_vectors_{category} (장기 취향)

### 2-1. 목적

- **협업 필터링**: 사용자 간 코사인 유사도 계산 → 비슷한 사용자 검색
- **콘텐츠 기반 필터링**: 사용자 벡터와 상품 벡터의 유사도 계산
- 변화가 느린 **장기 취향**을 누적·반영

### 2-2. 벡터 구성

카테고리별 feature 순서대로 0~1 값을 채운 배열.
예) `base_makeup`: `[matte, glow, long_lasting, coverage_high, lightweight, drying, cakey, oxidation]`

### 2-3. Payload 스키마

| 필드 | 타입 | 설명 | 인덱싱 |
|------|------|------|--------|
| `user_id` | string | 사용자 고유 ID (예: `u123`) | ✅ |
| `category` | string | 카테고리 (`base_makeup` 등) | ✅ |
| `skin_type` | string | 피부 타입 (건성/지성/복합성/민감성) | ✅ |
| `personal_color` | string | 퍼스널 컬러 (웜톤/쿨톤/뉴트럴) | ✅ |
| `age_range` | string | 연령대 (10s/20s/30s/40s+) | - |
| `updated_at` | string (ISO 8601) | 최근 업데이트 시각 | - |
| `interaction_count` | int | 누적 상호작용 횟수 | - |

### 2-4. Point 예시

```json
{
  "id": "u123",
  "vector": [0.8, 0.3, 0.9, 0.7, 0.6, 0.2, 0.1, 0.2],
  "payload": {
    "user_id": "u123",
    "category": "base_makeup",
    "skin_type": "건성",
    "personal_color": "웜톤",
    "age_range": "20s",
    "updated_at": "2026-05-12T10:30:00",
    "interaction_count": 47
  }
}
```

해석: 매트 선호도 0.8, 지속력 중시 0.9, 글로우는 별로(0.3), 건조함은 거의 없는 사용자.

### 2-5. 업데이트 규칙

```
사용자 벡터 = Σ (행동 가중치 × 해당 상품 벡터) + 구매 사유 기반 feature 보정
```

행동별 가중치:
- 클릭(click): 0.1
- 체류 시간(dwell): 0.2
- 장바구니(cart): 0.5
- 구매(purchase): 1.0

---

## 3. Collection 2: product_vectors_{category} (상품 특성)

### 3-1. 목적

- 상품의 객관적 특성을 feature 벡터로 표현
- 콘텐츠 기반 필터링에서 사용자 벡터와 유사도 계산
- 협업 필터링 후보 상품 추출 시 사용

### 3-2. 벡터 구성

`user_vectors_{category}`와 **동일한 feature 공간**을 사용해야 코사인 유사도 비교 가능.

### 3-3. Payload 스키마

| 필드 | 타입 | 설명 | 인덱싱 |
|------|------|------|--------|
| `product_id` | string | 상품 고유 ID (예: `p001`) | ✅ |
| `product_name` | string | 상품명 | - |
| `brand` | string | 브랜드명 (영어 대문자) | ✅ |
| `category_main` | string | 대분류 (`base_makeup` 등) | ✅ |
| `category_sub` | string | 소분류 (`liquid_foundation` 등) | ✅ |
| `volume` | int | 용량 (숫자) | - |
| `unit` | string | 단위 (`g` 또는 `ml`) | - |
| `shade` | string | 색상/호수 (nullable) | - |
| `key_ingredients` | array<string> | 주요 성분 | - |
| `warnings` | array<string> | 주의 성분 | - |
| `current_min_price` | int | 현재 최저가 (캐시) | - |
| `updated_at` | string (ISO 8601) | 최근 업데이트 시각 | - |

### 3-4. Point 예시

```json
{
  "id": "p001",
  "vector": [0.8, 0.3, 0.9, 0.7, 0.9, 0.4, 0.2, 0.1],
  "payload": {
    "product_id": "p001",
    "product_name": "실키 스테이 24H 롱웨어 파운데이션",
    "brand": "HERA",
    "category_main": "base_makeup",
    "category_sub": "liquid_foundation",
    "volume": 30,
    "unit": "g",
    "shade": null,
    "key_ingredients": ["히알루론산", "나이아신아마이드", "비타민 E"],
    "warnings": ["향료", "티타늄디옥사이드"],
    "current_min_price": 55440,
    "updated_at": "2026-05-12T10:30:00"
  }
}
```

해석: 매트 0.8 + 지속력 0.9 + 가벼움 0.9 → 종설 기획 예시(HERA 실키 스테이)의 특성과 일치.

### 3-5. 생성 출처

Agent 2(Gemini 또는 오픈소스 LLM)가 리뷰·성분 정보를 분석해 feature 벡터 생성 후 본 Collection에 저장. 이후 재사용을 위해 캐시.

---

## 4. Collection 3: purchase_reasons (구매 사유 - 단기 취향)

### 4-1. 목적

- **RAG 검색용**: 사용자가 입력한 구매 사유 텍스트를 임베딩으로 저장
- 신규 추천 요청 시 유사한 과거 사유를 검색하여 사용자의 **현재 의도** 추출
- 변화가 빠른 **단기 취향**(계절감, 상황별 선호 변화)을 반영
- Cold Start 완화 및 추천 설명 가능성 확보

### 4-2. 벡터 구성

| 항목 | 값 |
|------|-----|
| 차원 | 768 |
| 임베딩 모델 | `jhgan/ko-sroberta-multitask` |
| 거리 측정 | Cosine |

### 4-3. Payload 스키마

| 필드 | 타입 | 설명 | 인덱싱 |
|------|------|------|--------|
| `user_id` | string | 사용자 ID | ✅ |
| `product_id` | string | 구매한 상품 ID (nullable - 일반 의견인 경우) | ✅ |
| `category` | string | 카테고리 | ✅ |
| `reason_text` | string | 구매 사유 원문 | - |
| `input_type` | string | 입력 방식 (`free_text` / `selected_option`) | - |
| `season` | string | 작성 시점 계절 (`spring`/`summer`/`fall`/`winter`) | ✅ |
| `created_at` | string (ISO 8601) | 작성 시각 | - |
| `sentiment` | string | 감정 (`positive`/`negative`) | ✅ |

### 4-4. Point 예시

```json
{
  "id": "u123_p001_20260512",
  "vector": [0.0234, -0.0156, 0.0789, ...],
  "payload": {
    "user_id": "u123",
    "product_id": "p001",
    "category": "base_makeup",
    "reason_text": "지속력이 좋고 건조하지 않아서 구매했어요",
    "input_type": "free_text",
    "season": "spring",
    "created_at": "2026-05-12T14:22:00",
    "sentiment": "positive"
  }
}
```

### 4-5. ID 생성 규칙

`{user_id}_{product_id}_{YYYYMMDD}` 형식. 같은 사용자가 같은 상품을 여러 번 구매한 경우 날짜로 구분.

### 4-6. 검색 활용 흐름

```
[추천 요청 시]
1. 동일 user_id + 동일 category 필터로 과거 사유 Top-K 검색
2. 검색된 사유 텍스트들을 LLM에 전달 (LLM 담당자와 협업)
3. LLM이 feature 벡터로 변환 (예: long_lasting=0.9, drying=0.1)
4. 최종 사용자 벡터 = 기존 user_vector × 0.4 + RAG 결과 × 0.6
```

### 4-7. 입력 방식

기획 문서에 따라 두 가지 입력 방식 지원:

- **자유 텍스트 입력**: 사용자가 직접 작성
- **선택형 입력**: 카테고리별 predefined 옵션 (예: "지속력 좋음", "촉촉함", "커버력 우수")
  - 선택형도 내부적으로 텍스트로 변환 후 임베딩 (자유 텍스트와 동일한 벡터 공간에서 검색하기 위해)

---

## 5. Collection 간 관계 다이어그램

```
[Agent 1: VLM]
       │ 상품 정보 추출
       ▼
[Agent 2: 분석]──────────→ product_vectors_{category}
       │ feature 벡터 생성     (상품 캐시)
       │
       ▼
[Agent 3: 추천]
       │
       ├─ 협업 필터링 ─────→ user_vectors_{category} (유사 사용자 검색)
       │
       ├─ 콘텐츠 기반 ─────→ user_vectors_{category} × product_vectors_{category}
       │
       └─ RAG ──────────────→ purchase_reasons (유사 사유 검색)
                                      │
                                      ▼
                              LLM이 feature 벡터로 변환
                                      │
                                      ▼
                              user_vectors 보정
```

---

## 6. Indexing 전략

Qdrant의 payload index를 활용해 필터 검색 성능 최적화.

### 생성할 인덱스 (filter 검색에 자주 쓰이는 필드)

| Collection | Field | Index Type |
|------------|-------|------------|
| user_vectors_* | `user_id` | keyword |
| user_vectors_* | `skin_type` | keyword |
| user_vectors_* | `personal_color` | keyword |
| product_vectors_* | `product_id` | keyword |
| product_vectors_* | `brand` | keyword |
| product_vectors_* | `category_sub` | keyword |
| purchase_reasons | `user_id` | keyword |
| purchase_reasons | `category` | keyword |
| purchase_reasons | `season` | keyword |
| purchase_reasons | `sentiment` | keyword |

생성 예시 (Python):

```python
client.create_payload_index(
    collection_name="purchase_reasons",
    field_name="user_id",
    field_schema="keyword"
)
```

---

## 7. 본 프로젝트 시 확정/변경 가능 사항 (TODO)

본 프로젝트 시작 시 아키텍처 담당자와 협의해 확정할 항목.

- [ ] **카테고리별 feature 최종 검토**: Agent 2 (LLM 담당자)가 어떤 feature를 일관되게 추출 가능한지 확인 후 추가/제거
- [ ] **trend_vectors Collection 추가 여부**: 인스타그램 인플루언서 데이터 수집 시 별도 Collection 추가 (MVP 후순위)
- [ ] **벡터 정규화 방식**: feature가 모두 0~1이지만, 추가 정규화(L2 norm 등) 필요 여부 결정
- [ ] **하이브리드 가중치 튜닝**: 기존 user_vector vs RAG 결과 결합 비율 (현재 0.4 : 0.6) 실험 통해 조정
- [ ] **Cold Start 처리**: 신규 사용자의 초기 벡터를 설문 기반으로 어떻게 생성할지 (아키텍처 담당자와 협의)

---

## 8. 참고

- 종설 기획 문서: `종설_기획` (프로젝트 폴더)
- 멘토링 회의록: `기업_멘토링_회의_기록`
- 임베딩 모델 비교 결과: Day 1 산출물 (`03_embedding_compare.py` 실행 결과)
- 멘토 강조사항: "큐드란트는 스키마 데이터를 같이 넣을 수 있어 디버깅 용이" → payload에 풍부한 메타 정보 포함
