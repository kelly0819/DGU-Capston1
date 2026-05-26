-- Migration: 008_rpc_match_user_contexts
-- 목적: discovery_agent의 RAG 컨텍스트 검색 RPC
-- 의존: user_context_rags

CREATE OR REPLACE FUNCTION match_user_contexts(
    target_user_id  UUID,
    query_embedding vector(1024),
    match_category  VARCHAR DEFAULT NULL,
    match_count     INT     DEFAULT 5
)
RETURNS TABLE (
    context_text TEXT,
    similarity   FLOAT,
    created_at   TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    PERFORM set_config('hnsw.ef_search', '40', true);

    RETURN QUERY
    SELECT
        ucr.context_text,
        1 - (ucr.embedding <=> query_embedding) AS similarity,
        ucr.created_at
    FROM user_context_rags ucr
    WHERE
        ucr.user_id = target_user_id
        AND (match_category IS NULL OR ucr.category = match_category)
    ORDER BY ucr.embedding <=> query_embedding
    LIMIT match_count;
END;
$$;

GRANT EXECUTE ON FUNCTION match_user_contexts(UUID, vector, VARCHAR, INT) TO service_role;

COMMENT ON FUNCTION match_user_contexts IS '사용자의 과거 RAG 컨텍스트 검색. discovery_agent에서 사용';