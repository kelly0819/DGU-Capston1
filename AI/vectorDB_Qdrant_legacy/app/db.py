import os
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base


# 환경 변수에서 DB 주소 가져오기 (docker-compose.yml에서 주입함)
DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "postgresql://app:app@localhost:5432/cosmetics"
)


# 엔진: DB 연결 풀 관리
engine = create_engine(
    DATABASE_URL,
    pool_size=5,
    max_overflow=10,
    pool_pre_ping=True,
)


# 세션 만드는 공장
SessionLocal = sessionmaker(
    bind=engine,
    autoflush=False,
    autocommit=False,
)


# SQLAlchemy 모델 정의할 때 상속할 기본 클래스
Base = declarative_base()


def get_db():
    """FastAPI 의존성 주입용. 요청마다 새 세션을 만들고, 끝나면 닫는다."""
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()