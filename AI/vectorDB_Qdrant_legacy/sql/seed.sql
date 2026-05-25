-- 브랜드
INSERT INTO brand (brand_id, name) VALUES
    (1, '라네즈'),
    (2, '헤라'),
    (3, '에스티로더'),
    (4, '슈에무라'),
    (5, '조르지오 아르마니');

-- 사용자
INSERT INTO users (user_id, email, name, nickname) VALUES
    (1, 'minji@test.com', '김민지', '민지'),
    (2, 'jihye@test.com', '이지혜', '지혜'),
    (3, 'sumin@test.com', '박수민', '수민');

-- 사용자 프로필
INSERT INTO user_profile (user_id, personal_color, skin_type, skin_concern) VALUES
    (1, '쿨톤', '건성', '["수분부족", "민감성"]'::jsonb),
    (2, '웜톤', '복합성', '["모공", "유분"]'::jsonb),
    (3, '쿨톤', '지성', '["여드름", "유분과다"]'::jsonb);

-- 상품
INSERT INTO product (product_id, name, brand_id, category, price, rating, review_count, review_summary) VALUES
    (1, '워터 슬리핑 마스크', 1, 'skincare', 24500, 4.6, 1234, '수분 보충과 진정 효과가 좋음'),
    (2, '실키 스테이 24H 파운데이션', 2, 'base_makeup', 38000, 4.4, 856, '지속력과 커버력이 우수함'),
    (3, '더블 웨어 파운데이션', 3, 'base_makeup', 62000, 4.5, 2103, '커버력과 매트 마감이 강점'),
    (4, '언리미티드 라스팅', 4, 'base_makeup', 57000, 4.2, 432, '오래 가지만 다소 무거움'),
    (5, '루미너스 실크 파운데이션', 5, 'base_makeup', 79000, 4.7, 678, '광채와 발색이 뛰어남');

-- 상품 feature (베이스 메이크업 8차원: matte, glow, long_lasting, coverage_high, lightweight, drying, cakey, oxidation)
INSERT INTO product_feature (product_id, type, skin_type, feature_json) VALUES
    (2, 'liquid_foundation', '복합성', '{"matte": 0.7, "glow": 0.3, "long_lasting": 0.9, "coverage_high": 0.8, "lightweight": 0.5, "drying": 0.4, "cakey": 0.3, "oxidation": 0.2}'::jsonb),
    (3, 'liquid_foundation', '지성', '{"matte": 0.85, "glow": 0.15, "long_lasting": 0.95, "coverage_high": 0.9, "lightweight": 0.3, "drying": 0.5, "cakey": 0.4, "oxidation": 0.3}'::jsonb),
    (4, 'liquid_foundation', '지성', '{"matte": 0.85, "glow": 0.25, "long_lasting": 0.88, "coverage_high": 0.9, "lightweight": 0.25, "drying": 0.45, "cakey": 0.72, "oxidation": 0.35}'::jsonb),
    (5, 'liquid_foundation', '건성', '{"matte": 0.1, "glow": 0.95, "long_lasting": 0.65, "coverage_high": 0.7, "lightweight": 0.8, "drying": 0.15, "cakey": 0.1, "oxidation": 0.15}'::jsonb);

-- 스킨케어 feature (5차원: hydrating, soothing, non_sticky, fast_absorbing, irritation)
INSERT INTO product_feature (product_id, type, skin_type, feature_json) VALUES
    (1, 'sleeping_mask', '건성', '{"hydrating": 0.92, "soothing": 0.7, "non_sticky": 0.3, "fast_absorbing": 0.35, "irritation": 0.1}'::jsonb);

-- 리뷰
INSERT INTO review (product_id, content, rating, source) VALUES
    (1, '건조한 피부에 정말 좋아요. 끈적임이 조금 있지만 효과 만족.', 5.0, '올리브영'),
    (1, '아침에 일어나면 진짜 촉촉해요', 4.5, '올리브영'),
    (2, '지속력이 정말 좋습니다. 종일 무너지지 않아요', 4.5, '화해'),
    (3, '커버력 최고. 다만 살짝 매트해서 호불호 있을 듯', 4.5, '화해'),
    (5, '광채감이 자연스럽고 발색이 예뻐요', 5.0, '화해');

-- 사용자가 사용 중인 상품
INSERT INTO user_product (user_id, product_id, usage_type, rating) VALUES
    (1, 1, '사용 중', 5.0),
    (1, 2, '구매 고려', NULL),
    (2, 3, '사용 중', 4.5),
    (3, 4, '사용 중', 3.5);

-- 구매 사유 (RAG 원본 텍스트)
INSERT INTO user_context_rag (user_id, content, category) VALUES
    (1, '지속력이 좋고 건조하지 않아서 구매했어요', 'base_makeup'),
    (1, '수분감이 오래가는 게 좋아요', 'skincare'),
    (2, '커버력이 강하고 매트한 마감이 좋아서', 'base_makeup'),
    (3, '유분 잡아주고 종일 무너지지 않아서', 'base_makeup');

-- AUTO_INCREMENT 카운터 동기화
SELECT setval('brand_brand_id_seq', (SELECT MAX(brand_id) FROM brand));
SELECT setval('users_user_id_seq', (SELECT MAX(user_id) FROM users));
SELECT setval('product_product_id_seq', (SELECT MAX(product_id) FROM product));