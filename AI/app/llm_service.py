from openai import OpenAI
from dotenv import load_dotenv
import os
from pathlib import Path

# .env 로드
env_path = Path(__file__).resolve().parent.parent / ".env"
load_dotenv(env_path)

client = OpenAI(
    api_key=os.getenv("OPENAI_API_KEY"),  # 여기 MindLogic 키 넣으면 됨
    base_url="https://factchat-cloud.mindlogic.ai/v1/gateway",
)

def ask_llm(prompt: str) -> str:
    response = client.chat.completions.create(
        model="gpt-5.4-nano",  # 문서에 나온 모델
        messages=[
            {"role": "user", "content": prompt}
        ],
    )

    return response.choices[0].message.content