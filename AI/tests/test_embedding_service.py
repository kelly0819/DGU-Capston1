"""bge-m3 임베딩 서비스 테스트."""
import math
import pytest

from services.embedding_service import EmbeddingService


@pytest.fixture(scope="module")
def emb():
    return EmbeddingService.get()


class TestEmbeddingService:
    def test_singleton(self):
        assert EmbeddingService.get() is EmbeddingService.get()
        assert EmbeddingService() is EmbeddingService.get()

    def test_embed_dimension(self, emb):
        vec = emb.embed("쿠션 제품으로 커버력이 중간이며 매트 마무리감")
        assert len(vec) == 1024

    def test_embed_normalized(self, emb):
        vec = emb.embed("지성 피부에 적합한 베이스 메이크업")
        norm = math.sqrt(sum(v * v for v in vec))
        assert abs(norm - 1.0) < 1e-3

    def test_embed_batch(self, emb):
        vecs = emb.embed_batch(["쿠션", "선크림", "토너"])
        assert len(vecs) == 3
        assert all(len(v) == 1024 for v in vecs)

    def test_embed_empty_raises(self, emb):
        with pytest.raises(ValueError):
            emb.embed("")

    def test_embed_batch_empty_list(self, emb):
        assert emb.embed_batch([]) == []

    def test_model_version(self, emb):
        assert "bge-m3" in emb.model_version