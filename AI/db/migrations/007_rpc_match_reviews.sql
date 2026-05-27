-- Migration: 007_rpc_match_reviews
-- 목적: score_agent의 review_score 계산 RPC
-- 의존: review_embeddings

CREATE OR REPLACE FUNCTION match_reviews(
    query_embedding   vector(1024),
    target_product_id UUID,
    match_count       INT DEFAULT 10
)
RETURNS TABLE (
    review_id   UUID,
    review_text TEXT,
    similarity  FLOAT
)
LANGUAGE plpgsql
AS $$
BEGIN
    PERFORM set_config('hnsw.ef_search', '40', true);

    RETURN QUERY
    SELECT
        re.id,
        re.review_text,
        1 - (re.embedding <=> query_embedding) AS similarity
    FROM review_embeddings re
    WHERE re.product_id = target_product_id
    ORDER BY re.embedding <=> query_embedding
    LIMIT match_count;
END;
$$;

GRANT EXECUTE ON FUNCTION match_reviews(vector, UUID, INT) TO service_role;

COMMENT ON FUNCTION match_reviews IS '특정 상품의 리뷰 중 intent_vector와 가장 가까운 N개 반환. review_score 계산용';