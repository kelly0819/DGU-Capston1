from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    DASHSCOPE_API_KEY: str
    QWEN_VL_BASE_URL: str = "https://dashscope-intl.aliyuncs.com/compatible-mode/v1"
    QWEN_VL_MODEL: str = "qwen2.5-vl-7b-instruct"

    GEMINI_API_KEY: str
    GEMINI_MODEL: str = "gemini-2.0-flash"

    # Supabase
    SUPABASE_URL: str
    SUPABASE_SERVICE_KEY: str

    # Embedding 
    EMBEDDING_MODEL: str = "BAAI/bge-m3"
    EMBEDDING_DEVICE: str = "cpu"
    EMBEDDING_USE_FP16: bool = False
    HF_HOME: str = "/app/.cache/huggingface"

    # Vector search
    VECTOR_SEARCH_EF_SEARCH: int = 40

    # Monitoring
    LANGSMITH_API_KEY: str = ""

    class Config:
        env_file = ".env"
        extra = "ignore"


settings = Settings()
