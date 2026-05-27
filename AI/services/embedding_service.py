"""
bge-m3 임베딩 서비스 싱글톤.

bge-m3 모델은 ~2.3GB이고 콜드스타트가 5~10초 걸리므로,
요청마다 새로 로드하지 않도록 싱글톤으로 관리한다.

모든 임베딩은 L2 정규화된 1024차원 벡터로 반환된다
(pgvector cosine distance 안정성 확보).

사용 예:
    from services.embedding_service import EmbeddingService

    emb = EmbeddingService.get()
    vec = emb.embed("쿠션 제품으로 커버력이 높은...")
    vecs = emb.embed_batch(["text1", "text2", "text3"])
"""
from __future__ import annotations

from typing import List, Optional

from FlagEmbedding import BGEM3FlagModel

from config import settings


class EmbeddingService:
    """bge-m3 임베딩 싱글톤. L2 정규화된 1024차원 벡터 반환."""

    _instance: Optional["EmbeddingService"] = None
    _model: Optional[BGEM3FlagModel] = None
    _model_version: str = "bge-m3-v1.5"

    def __new__(cls) -> "EmbeddingService":
        # __new__로 싱글톤 보장. EmbeddingService()와 EmbeddingService.get() 모두 동일 인스턴스
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    @classmethod
    def get(cls) -> "EmbeddingService":
        """싱글톤 인스턴스 반환. EmbeddingService()와 동등."""
        return cls()

    def _load(self) -> None:
        """모델 lazy load. 첫 embed 호출 시에만 로딩."""
        if self._model is None:
            self._model = BGEM3FlagModel(
                settings.EMBEDDING_MODEL,
                use_fp16=settings.EMBEDDING_USE_FP16,
                normalize_embeddings=True,  # L2 정규화. pgvector cosine 안정성
            )

    def embed(self, text: str) -> List[float]:
        """단일 텍스트 → 1024차원 정규화 벡터."""
        if not text or not text.strip():
            raise ValueError("빈 텍스트는 임베딩할 수 없습니다")
        self._load()
        result = self._model.encode(
            [text],
            batch_size=1,
            max_length=1024,
            return_dense=True,
        )
        return result["dense_vecs"][0].tolist()

    def embed_batch(
        self,
        texts: List[str],
        batch_size: int = 32,
    ) -> List[List[float]]:
        """여러 텍스트 배치 임베딩. 빈 리스트는 빈 리스트 반환."""
        if not texts:
            return []
        filtered = [t for t in texts if t and t.strip()]
        if not filtered:
            raise ValueError("유효한 텍스트가 없습니다")
        self._load()
        result = self._model.encode(
            filtered,
            batch_size=batch_size,
            max_length=1024,
            return_dense=True,
        )
        return [v.tolist() for v in result["dense_vecs"]]

    @property
    def model_version(self) -> str:
        """모델 버전 식별자. product_embeddings.model_version 컬럼에 저장."""
        return self._model_version