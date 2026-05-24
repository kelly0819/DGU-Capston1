import os
from qdrant_client import QdrantClient
from qdrant_client.models import Distance, VectorParams


QDRANT_URL = os.getenv("QDRANT_URL", "http://localhost:6333")


# 한 번만 만들고 모듈 전역에서 재사용
client = QdrantClient(url=QDRANT_URL)


# Collection 설정 정의 (init_collections.py에서 사용!)
COLLECTION_CONFIG = {
    "user_vectors_base_makeup": {"size": 8,  "distance": Distance.COSINE},
    "user_vectors_skincare": {"size": 5,  "distance": Distance.COSINE},
    "user_vectors_lip_eye": {"size": 4,  "distance": Distance.COSINE},
    "product_vectors_base_makeup": {"size": 8, "distance": Distance.COSINE},
    "product_vectors_skincare": {"size": 5, "distance": Distance.COSINE},
    "product_vectors_lip_eye": {"size": 4, "distance": Distance.COSINE},
    "review_embeddings": {"size": 768, "distance": Distance.COSINE},
    "purchase_reasons": {"size": 768, "distance": Distance.COSINE},
}


# 카테고리별 feature 키 순서 (Python dict -> vector 변환 시 사용!)
FEATURE_KEYS = {
    "base_makeup": [
        "matte", "glow", "long_lasting", "coverage_high",
        "lightweight", "drying", "cakey", "oxidation",
    ],
    "skincare": [
        "hydrating", "soothing", "non_sticky", "fast_absorbing", "irritation",
    ],
    "lip_eye": [
        "pigmentation", "long_lasting", "smudge_proof", "drying",
    ],
}


# feature JSON을 그대로 배열로 변환 
def feature_dict_to_vector(category: str, feature_json: dict) -> list[float]:
    """{'matte': 0.8, 'glow': 0.2, ...} → [0.8, 0.2, ...]
    
    정해진 순서대로 값을 빼서 리스트로 만든다.
    없는 키는 0.0으로 채움 (Agent2가 일부 누락해도 깨지지 않도록)."""
    keys = FEATURE_KEYS[category]
    return [float(feature_json.get(k, 0.0)) for k in keys]


def get_vector_collection_name(category: str, kind: str) -> str:
    """('base_makeup', 'product') → 'product_vectors_base_makeup' """
    return f"{kind}_vectors_{category}"