-- =========================================
-- 사용자 그룹
-- =========================================

CREATE TABLE users (
    user_id     BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) UNIQUE NOT NULL,
    name        VARCHAR(100) NOT NULL,
    nickname    VARCHAR(50),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_profile (
    user_id         BIGINT PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    personal_color  VARCHAR(20),
    skin_type       VARCHAR(20),
    skin_concern    JSONB,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- 상품 그룹
-- =========================================

CREATE TABLE brand (
    brand_id    BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE product (
    product_id      BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    brand_id        BIGINT REFERENCES brand(brand_id) ON DELETE SET NULL,
    category        VARCHAR(50) NOT NULL,
    image_url       TEXT,
    price           INT,
    rating          FLOAT,
    review_count    INT DEFAULT 0,
    review_summary  TEXT,
    last_updated    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_product_category ON product(category);
CREATE INDEX idx_product_brand    ON product(brand_id);

CREATE TABLE product_feature (
    feature_id      BIGSERIAL PRIMARY KEY,
    product_id      BIGINT NOT NULL REFERENCES product(product_id) ON DELETE CASCADE,
    type            VARCHAR(50) NOT NULL,
    skin_type       VARCHAR(20),
    skin_concern    JSONB,
    feature_json    JSONB NOT NULL
);

CREATE INDEX idx_pf_product   ON product_feature(product_id);
CREATE INDEX idx_pf_type      ON product_feature(type);
CREATE INDEX idx_pf_skin_type ON product_feature(skin_type);

-- =========================================
-- 브릿지 (다대다)
-- =========================================

CREATE TABLE user_brand_preference (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    brand_id    BIGINT NOT NULL REFERENCES brand(brand_id) ON DELETE CASCADE,
    UNIQUE (user_id, brand_id)
);

CREATE TABLE user_product (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    product_id  BIGINT NOT NULL REFERENCES product(product_id) ON DELETE CASCADE,
    usage_type  VARCHAR(20) NOT NULL,
    rating      FLOAT,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_up_user    ON user_product(user_id);
CREATE INDEX idx_up_product ON user_product(product_id);

-- =========================================
-- 리뷰 그룹
-- =========================================

CREATE TABLE review (
    review_id   BIGSERIAL PRIMARY KEY,
    product_id  BIGINT NOT NULL REFERENCES product(product_id) ON DELETE CASCADE,
    content     TEXT NOT NULL,
    rating      FLOAT,
    source      VARCHAR(50),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_review_product ON review(product_id);

-- =========================================
-- RAG (구매 사유 텍스트 원본)
-- =========================================

CREATE TABLE user_context_rag (
    rag_id      BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    content     TEXT NOT NULL,
    category    VARCHAR(50),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ucr_user     ON user_context_rag(user_id);
CREATE INDEX idx_ucr_category ON user_context_rag(category);