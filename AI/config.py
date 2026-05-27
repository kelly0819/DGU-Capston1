from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    DASHSCOPE_API_KEY: str
    QWEN_VL_BASE_URL: str = "https://dashscope-intl.aliyuncs.com/compatible-mode/v1"
    QWEN_VL_MODEL: str = "qwen2.5-vl-7b-instruct"

    GEMINI_API_KEY: str
    GEMINI_MODEL: str = "gemini-2.0-flash"

    class Config:
        env_file = ".env"


settings = Settings()
