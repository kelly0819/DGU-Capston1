# Cosmetics Recommender AI

> AI 기반 화장품 추천 시스템 — 사용자의 피부 타입, 퍼스널 컬러, 단기 취향을 결합해 화장품을 추천합니다.

본 프로젝트는 사용자가 상품 이미지를 촬영하거나 URL을 입력하면, **VLM이 상품 정보를 추출**하고, **LLM이 가격·리뷰·성분을 분석**한 뒤, **벡터 DB 기반 협업 필터링과 콘텐츠 기반 추천**을 통해 개인화된 결과를 제공하는 시스템입니다.

장기 취향(피부 타입, 퍼스널 컬러)과 단기 취향(트렌드, 계절감)을 함께 반영하며, RAG를 활용해 추천 근거를 자연어로 설명합니다.

---

## 팀 구성

| 이름 | 역할 |
|------|------|
|      |      |
|      |      |
|      |      |
|      |      |

---

## 기술 스택

| 분야 | 사용 기술 |
|------|----------|
| AI Server | Python 3.11+, FastAPI |
| Database | PostgreSQL 16, Qdrant |
| LLM | Ollama, EXAONE 3.5, Qwen 2.5, Llama 3.1 |
| VLM | Qwen2-VL, Llama 3.2 Vision |
| Embedding | bge-m3, Ko-SRoBERTa |
| Backend | _TBD_ |
| Frontend | _TBD_ |
| Infra | Docker, Docker Compose |

---

## 레포지토리 구조

```
DGU-Capston1/
├── AI/                    # AI 모듈 (모노레포 형태)
│   ├── shared/            # AI 모듈 공통 코드 (DB 모델, config, 공통 스키마)
│   ├── vectordb/          # 벡터 DB 모듈 (Qdrant, 임베딩, 검색 API)
│   ├── llm/               # LLM 모듈 (Ollama, 프롬프트, reasoning)
│   ├── vlm/               # VLM 모듈 (이미지 분석, OCR, 상품 정보 추출)
│   ├── architecture/      # 오케스트레이션 모듈 (Agent 라우팅, RAG)
│   └── docker-compose.yml
├── BE/                    # 백엔드 서버
├── FE/                    # 프론트엔드 (모바일 앱)
├── .gitignore
├── LICENSE
└── README.md
```

---

## 시작하기

### 사전 요구사항

- Git, Docker, Docker Compose
- Python 3.11+ (AI 모듈 로컬 작업 시)

### 설치 및 실행

```bash
# 1. 레포 클론
git clone https://github.com/kelly0819/DGU-Capston1.git
cd DGU-Capston1

# 2. 환경 변수 설정
cp .env.example .env
# .env 파일을 본인 환경에 맞게 수정

# 3. AI 모듈 실행
cd AI
docker compose up -d
```

> ⚠️ `.env` 와 `venv/` 는 절대 커밋하지 마세요.

---

## 개발 컨벤션

### 1. 브랜치 전략

```
main (시연·발표 가능한 안정 버전)
 ↑   ← develop → main PR (마일스톤 시점)
 │
develop (모든 개발 작업의 통합 브랜치)
 ↑   ← 작업 브랜치 → develop PR
 │
 ├── feature/vectordb-product-upsert
 ├── fix/login-crash
 └── docs/readme-update
```

#### 브랜치 역할

| 브랜치 | 역할 |
|--------|------|
| `main` | 시연·발표 가능한 안정 상태. 마일스톤 시점에만 머지됨 |
| `develop` | 모든 개발 작업의 통합 지점. 항상 CI 통과 상태 유지 |
| `feature/*`, `fix/*` 등 | 개인 작업 브랜치. `develop`에서 분기 |
| `hotfix/*` | `main` 긴급 수정용. `main`에서 분기 후 `main`과 `develop` 양쪽에 반영 |

#### 브랜치 명명 규칙

```
<type>/<짧은-설명>
```

| Type | 용도 | 베이스 |
|------|------|--------|
| `feature` | 새 기능 개발 | `develop` |
| `fix` | 버그 수정 | `develop` |
| `refactor` | 기능 변화 없는 리팩토링 | `develop` |
| `docs` | 문서 작업 | `develop` |
| `chore` | 빌드, 설정, 의존성 | `develop` |
| `test` | 테스트 추가/수정 | `develop` |
| `hotfix` | 긴급 버그 수정 | `main` |

#### 작업 흐름

```bash
# develop에서 분기
git checkout develop
git pull origin develop
git checkout -b feature/my-task

# 작업, 커밋, 푸시
git add .
git commit -m "feat: 기능 추가"
git push -u origin feature/my-task

# GitHub에서 develop 대상으로 PR 생성
```

#### 브랜치 규칙

- `main`과 `develop`으로의 **직접 푸시 금지** (PR 필수)
- 머지 후 작업 브랜치 **즉시 삭제**
- 작업 브랜치는 **3~5일 이내로 짧게** 유지
- 작업 도중 `develop`이 갱신되면 자주 동기화:

```bash
git fetch origin
git rebase origin/develop
```

---

### 2. 커밋 메시지 규칙

[Conventional Commits](https://www.conventionalcommits.org/ko/) 형식을 따릅니다.

#### 형식

```
<type>: <subject>
```

#### Type

| Type | 설명 |
|------|------|
| `feat` | 새 기능 추가 |
| `fix` | 버그 수정 |
| `docs` | 문서 변경 |
| `style` | 코드 의미에 영향 없는 변경 (포매팅, 세미콜론 등) |
| `refactor` | 기능 변화 없는 코드 리팩토링 |
| `perf` | 성능 개선 |
| `test` | 테스트 코드 추가/수정 |
| `chore` | 빌드, 패키지, 설정 등 |

---

### 3. Pull Request

코드 리뷰의 대부분은 **`develop` 대상 PR**에서 이루어집니다. `main` 대상 PR은 마일스톤 시점의 통합 점검 성격입니다.

| PR 방향 | 시점 | 리뷰 | 머지 전략 |
|---------|------|------|----------|
| 작업 브랜치 → `develop` | 상시 | **1명 승인** (코드 리뷰) | **Squash and merge** |
| `develop` → `main` | 마일스톤 (MVP/중간/최종 발표 등) | **전원 승인** (릴리스 점검) | **Merge commit** |

#### 공통 규칙

- CI 통과 후 머지 가능
- 머지 후 작업 브랜치 자동 삭제
- `main`, `develop` 으로의 직접 푸시 금지 (PR 필수)