"""
간단한 통합 테스트.

사용법 (도커 + init_collections + reindex_all 끝낸 뒤):
    docker compose exec api python -m pytest tests/ -v

또는 그냥 호스트에서 requests로:
    python tests/test_search.py
"""
import requests


BASE_URL = "http://localhost:8000"


def test_health():
    r = requests.get(f"{BASE_URL}/health")
    assert r.status_code == 200
    assert r.json() == {"status": "ok"}
    print("✅ health check")


def test_get_user_vector():
    r = requests.get(f"{BASE_URL}/users/1/vector", params={"category": "base_makeup"})
    assert r.status_code == 200
    data = r.json()
    assert data["user_id"] == 1
    assert len(data["vector"]) == 8
    print(f"✅ user 1 vector: {data['vector']}")


def test_similar_products():
    user_vec = requests.get(
        f"{BASE_URL}/users/1/vector",
        params={"category": "base_makeup"}
    ).json()["vector"]
    
    r = requests.post(f"{BASE_URL}/search/similar-products", json={
        "user_vector": user_vec,
        "category": "base_makeup",
        "top_k": 3,
    })
    assert r.status_code == 200
    results = r.json()["results"]
    assert len(results) > 0
    print(f"✅ found {len(results)} similar products:")
    for p in results:
        print(f"    score={p['score']:.3f} {p['brand_name']} {p['name']} ({p['price']}원)")


def test_retrieve_reasons():
    r = requests.post(f"{BASE_URL}/reasons/retrieve", json={
        "user_id": 1,
        "category": "base_makeup",
        "query_text": "오래 가는 제품",
        "top_k": 3,
    })
    assert r.status_code == 200
    reasons = r.json()["reasons"]
    print(f"✅ found {len(reasons)} reasons:")
    for reason in reasons:
        print(f"    score={reason['score']:.3f} {reason['text']}")


def test_similar_users():
    r = requests.post(f"{BASE_URL}/search/similar-users", json={
        "user_id": 1,
        "category": "base_makeup",
        "top_k": 5,
    })
    assert r.status_code == 200
    users = r.json()["similar_users"]
    print(f"✅ found {len(users)} similar users:")
    for u in users:
        print(f"    score={u['score']:.3f} user_id={u['user_id']}")


if __name__ == "__main__":
    test_health()
    test_get_user_vector()
    test_similar_products()
    test_retrieve_reasons()
    test_similar_users()
    print("\n✅ All tests passed ✅")