import random
from qdrant_utils import VectorDB

db = VectorDB()

# 베이스 메이크업 feature 8차원으로 통일
FEATURES = ["matte", "glow", "long_lasting", "coverage_high",
            "lightweight", "drying", "cakey", "oxidation"]

db.create_collection("user_vectors", size=8)
db.create_collection("product_vectors", size=8)

# 3가지 페르소나로 사용자 50명 만들기
def make_user_vector(persona):
    if persona == "matte_lover":      # 매트 + 지속력 중시
        return [random.uniform(0.7,1.0), random.uniform(0.0,0.3),
                random.uniform(0.7,1.0), random.uniform(0.5,0.8),
                random.uniform(0.4,0.7), random.uniform(0.2,0.5),
                random.uniform(0.1,0.3), random.uniform(0.0,0.2)]
    elif persona == "glow_lover":     # 글로우 + 가벼움 중시
        return [random.uniform(0.0,0.3), random.uniform(0.7,1.0),
                random.uniform(0.4,0.7), random.uniform(0.3,0.6),
                random.uniform(0.7,1.0), random.uniform(0.0,0.3),
                random.uniform(0.0,0.2), random.uniform(0.1,0.3)]
    else:                              # natural
        return [random.uniform(0.3,0.6) for _ in range(8)]

users = []
for i in range(50):
    persona = random.choice(["matte_lover", "glow_lover", "natural"])
    users.append({
        "id": i+1,
        "vector": make_user_vector(persona),
        "payload": {
            "user_id": f"u{i+1:03d}",
            "persona": persona,
            "skin_type": random.choice(["건성", "지성", "복합성"]),
            "category": "base_makeup"
        }
    })
db.upsert_batch("user_vectors", users)

# 상품 50개도 비슷한 방식으로 생성
products = []
for i in range(50):
    persona = random.choice(["matte_lover", "glow_lover", "natural"])
    products.append({
        "id": i+1,
        "vector": make_user_vector(persona),  # 같은 함수 재활용
        "payload": {
            "product_id": f"p{i+1:03d}",
            "brand": random.choice(["HERA", "LANEIGE", "INNISFREE"]),
            "category": "base_makeup",
            "type": persona  # 검증용
        }
    })
db.upsert_batch("product_vectors", products)
print("더미 데이터 생성 완료")