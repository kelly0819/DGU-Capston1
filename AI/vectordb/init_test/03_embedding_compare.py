from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity
import time

# 테스트 문장들 (의미가 비슷한 쌍, 다른 쌍)
sentences = [
    "지속력이 좋아서 구매했어요",           # 0
    "오래 가는 제품이라 좋아요",            # 1 (0과 유사한 의미)
    "촉촉하고 부드러워요",                  # 2 (다른 의미)
    "건조해서 별로예요",                    # 3 (2와 반대)
]

models_to_test = [
    "BAAI/bge-m3",                  # 다국어
    "jhgan/ko-sroberta-multitask",  # 한국어 특화
    "BAAI/bge-small-en-v1.5",       # 경량 (영어 위주지만 참고용)
]

for model_name in models_to_test:
    print(f"\n===== {model_name} =====")
    start = time.time()
    model = SentenceTransformer(model_name) # 모델 불러오기
    load_time = time.time() - start
    
    start = time.time()
    embeddings = model.encode(sentences)
    encode_time = time.time() - start
    
    print(f"로딩 시간: {load_time:.2f}초 / 임베딩 시간: {encode_time:.2f}초")
    print(f"벡터 차원: {embeddings.shape[1]}") # 벡터가 숫자 몇 개짜리인지 출력 (차원이 클수록 더 많은 정보)
    
    sim_0_1 = cosine_similarity([embeddings[0]], [embeddings[1]])[0][0]
    sim_0_2 = cosine_similarity([embeddings[0]], [embeddings[2]])[0][0]
    sim_2_3 = cosine_similarity([embeddings[2]], [embeddings[3]])[0][0]
    
    print(f"'지속력 좋아' vs '오래 가는' (유사해야 함): {sim_0_1:.4f}")
    print(f"'지속력 좋아' vs '촉촉해'   (달라야 함): {sim_0_2:.4f}")
    print(f"'촉촉해'     vs '건조해'   (의미반대): {sim_2_3:.4f}")