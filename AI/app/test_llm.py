from llm_service import ask_llm

if __name__ == "__main__":
    result = ask_llm("건성 피부에 좋은 화장품 추천해줘")
    print("AI 응답:")
    print(result)