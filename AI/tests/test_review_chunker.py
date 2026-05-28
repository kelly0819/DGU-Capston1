"""review_chunker 단위 테스트."""
from services.review_chunker import chunk_review, clean_noise


class TestReviewChunker:
    def test_short_review_excluded(self):
        # 10자 미만은 제외
        assert chunk_review("좋아요") == []

    def test_normal_review_single_chunk(self):
        text = "지성 피부에 잘 맞고 발색도 정말 좋습니다. 강력 추천해요!"
        result = chunk_review(text)
        assert len(result) == 1
        assert "지성 피부" in result[0]

    def test_emoji_removed(self):
        text = "정말 좋아요 😍😍😍 최고 👍 추천합니다"
        cleaned = clean_noise(text)
        assert "😍" not in cleaned
        assert "👍" not in cleaned
        assert "정말 좋아요" in cleaned

    def test_url_removed(self):
        text = "참고: https://example.com 정말 좋은 제품이에요 색감이 자연스러워요"
        cleaned = clean_noise(text)
        assert "https://" not in cleaned
        assert "좋은 제품" in cleaned

    def test_repeat_hangul_normalized(self):
        text = "이거 진짜 ㅋㅋㅋㅋㅋ 좋은 제품이라 다시 사고 싶을 정도예요"
        cleaned = clean_noise(text)
        assert "ㅋㅋㅋㅋㅋ" not in cleaned
        assert "ㅋ" in cleaned  # 1회로 정규화

    def test_long_review_split_into_chunks(self):
        sentence = "이 제품은 정말 자연스럽고 발림성이 좋습니다 색감도 마음에 들고 지속력도 만족스럽네요."
        text = " ".join([sentence] * 10)
        result = chunk_review(text)
        assert len(result) >= 2
        for chunk in result:
            assert len(chunk) <= 300

    def test_empty_after_noise_removal(self):
        # 이모지만 있는 경우
        assert chunk_review("😀😀😀😀😀") == []