"""unified_text_builder 단위 테스트."""
from models.agent_context import UserProfile
from models.extracted_product import ExtractedProduct
from services.unified_text_builder import build_unified_text


def _profile():
    return UserProfile(
        user_id="user-uuid",
        skin_type="OILY",
        personal_color="SPRING_WARM",
        skin_concerns=["ACNE", "SEBUM"],
    )


def _product():
    return ExtractedProduct(
        product_name="네오쿠션",
        brand="LANEIGE",
        category={"main": "base", "sub": "쿠션"},
    )


class TestUnifiedTextBuilder:
    def test_full_context(self):
        text = build_unified_text(
            user_profile=_profile(),
            base_product=_product(),
            search_purpose="DAILY",
            price_tolerance_percent=10,
            rag_contexts=["세미매트 쿠션 조회", "선크림 검색"],
            query="여름에 무너지지 않는 베이스",
        )
        assert "OILY 피부" in text
        assert "SPRING_WARM 톤" in text
        assert "ACNE·SEBUM" in text
        assert "데일리" in text
        assert "LANEIGE 네오쿠션" in text
        assert "base 카테고리" in text
        assert "±10%" in text
        assert "세미매트 쿠션 조회" in text
        assert "여름에 무너지지 않는 베이스" in text

    def test_minimal_context(self):
        # RAG, search_purpose, query 없어도 동작
        text = build_unified_text(
            user_profile=_profile(),
            base_product=_product(),
        )
        assert "네오쿠션" in text
        assert "OILY 피부" in text
        assert "과거에" not in text
        assert "구매 사유" not in text

    def test_purpose_label_translation(self):
        text = build_unified_text(
            user_profile=_profile(),
            base_product=_product(),
            search_purpose="GIFT",
        )
        assert "선물용" in text

    def test_purpose_unknown_passes_through(self):
        text = build_unified_text(
            user_profile=_profile(),
            base_product=_product(),
            search_purpose="UNKNOWN_PURPOSE",
        )
        # 알 수 없는 목적은 원문 그대로
        assert "UNKNOWN_PURPOSE" in text

    def test_extracted_product_dict_category_handled(self):
        # ExtractedProduct.category가 dict인 LLM/VLM 형식 처리
        text = build_unified_text(
            user_profile=_profile(),
            base_product=_product(),
        )
        assert "base 카테고리" in text

    def test_empty_profile_concerns(self):
        profile = UserProfile(
            user_id="user-uuid",
            skin_type="DRY",
            personal_color=None,
            skin_concerns=[],
        )
        text = build_unified_text(
            user_profile=profile,
            base_product=_product(),
        )
        assert "DRY 피부" in text
        assert "고민" not in text