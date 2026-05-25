from sentence_transformers import SentenceTransformer


# 모델 이름 (03_embedding_compare.py에서 검증한 모델)
EMBEDDING_MODEL_NAME = "jhgan/ko-sroberta-multitask"
EMBEDDING_DIM = 768


# 앱 시작 시 한 번만 로드 (느리니까 매번 만들지 않음)
_model: SentenceTransformer | None = None


def get_model() -> SentenceTransformer:
    """모델을 처음 호출할 때 로드하고, 이후엔 캐시된 걸 반환."""
    global _model
    if _model is None:
        _model = SentenceTransformer(EMBEDDING_MODEL_NAME)
    return _model


def embed_text(text: str) -> list[float]:
    """문장 하나를 768차원 벡터로 변환."""
    model = get_model()
    vector = model.encode(text)
    return vector.tolist()


def embed_texts(texts: list[str]) -> list[list[float]]:
    """여러 문장을 한 번에 임베딩 (배치 처리, 더 빠름)."""
    model = get_model()
    vectors = model.encode(texts)
    return vectors.tolist()