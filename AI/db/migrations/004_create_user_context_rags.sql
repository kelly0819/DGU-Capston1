-- Migration: 004_create_user_context_rags
-- 목적: 사용자 맥락 RAG 임베딩 테이블 + 복합 인덱스 + HNSW
-- 의존: pgvector 확장, users 테이블

CREATE TABLE IF NOT EXISTS user_context_rags (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id       UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    context_text  TEXT         NOT NULL,
    embedding     vector(1024) NOT NULL,
    category      VARCHAR(20),
    created_at    TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_user_context_rags_user_cat
    ON user_context_rags (user_id, category, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_user_context_rags_hnsw
    ON user_context_rags
    USING hnsw (embedding vector_cosine_ops)
    WITH (m = 16, ef_construction = 64);

COMMENT ON TABLE user_context_rags IS '사용자 과거 조회·구매 사유 맥락의 통합 자연어 임베딩. discovery_agent RAG에 사용';
COMMENT ON COLUMN user_context_rags.context_text IS 'query + profile + 과거 맥락 통합 자연어 (unified_text_builder 출력)';
COMMENT ON COLUMN user_context_rags.category IS '조회 시점 카테고리 (base/sun/lip/skincare). RAG 필터링용';