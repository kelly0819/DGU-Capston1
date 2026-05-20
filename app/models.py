from sqlalchemy import (
    Column, BigInteger, String, Text, Integer, Float,
    TIMESTAMP, ForeignKey, UniqueConstraint, func,
)
from sqlalchemy.dialects.postgresql import JSONB
from sqlalchemy.orm import relationship

from db import Base


class User(Base):
    __tablename__ = "users"

    user_id    = Column(BigInteger, primary_key=True, autoincrement=True)
    email      = Column(String(255), unique=True, nullable=False)
    name       = Column(String(100), nullable=False)
    nickname   = Column(String(50))
    created_at = Column(TIMESTAMP, nullable=False, server_default=func.current_timestamp())


class UserProfile(Base):
    __tablename__ = "user_profile"

    user_id        = Column(BigInteger, ForeignKey("users.user_id", ondelete="CASCADE"), primary_key=True)
    personal_color = Column(String(20))
    skin_type      = Column(String(20))
    skin_concern   = Column(JSONB)
    updated_at     = Column(TIMESTAMP, nullable=False, server_default=func.current_timestamp())


class Brand(Base):
    __tablename__ = "brand"

    brand_id = Column(BigInteger, primary_key=True, autoincrement=True)
    name     = Column(String(100), unique=True, nullable=False)


class Product(Base):
    __tablename__ = "product"

    product_id     = Column(BigInteger, primary_key=True, autoincrement=True)
    name           = Column(String(255), nullable=False)
    brand_id       = Column(BigInteger, ForeignKey("brand.brand_id", ondelete="SET NULL"))
    category       = Column(String(50), nullable=False)
    image_url      = Column(Text)
    price          = Column(Integer)
    rating         = Column(Float)
    review_count   = Column(Integer, default=0)
    review_summary = Column(Text)
    last_updated   = Column(TIMESTAMP, nullable=False, server_default=func.current_timestamp())

    brand = relationship("Brand")


class ProductFeature(Base):
    __tablename__ = "product_feature"

    feature_id   = Column(BigInteger, primary_key=True, autoincrement=True)
    product_id   = Column(BigInteger, ForeignKey("product.product_id", ondelete="CASCADE"), nullable=False)
    type         = Column(String(50), nullable=False)
    skin_type    = Column(String(20))
    skin_concern = Column(JSONB)
    feature_json = Column(JSONB, nullable=False)


class Review(Base):
    __tablename__ = "review"

    review_id  = Column(BigInteger, primary_key=True, autoincrement=True)
    product_id = Column(BigInteger, ForeignKey("product.product_id", ondelete="CASCADE"), nullable=False)
    content    = Column(Text, nullable=False)
    rating     = Column(Float)
    source     = Column(String(50))
    created_at = Column(TIMESTAMP, nullable=False, server_default=func.current_timestamp())


class UserProduct(Base):
    __tablename__ = "user_product"

    id         = Column(BigInteger, primary_key=True, autoincrement=True)
    user_id    = Column(BigInteger, ForeignKey("users.user_id", ondelete="CASCADE"), nullable=False)
    product_id = Column(BigInteger, ForeignKey("product.product_id", ondelete="CASCADE"), nullable=False)
    usage_type = Column(String(20), nullable=False)
    rating     = Column(Float)
    created_at = Column(TIMESTAMP, nullable=False, server_default=func.current_timestamp())


class UserContextRag(Base):
    __tablename__ = "user_context_rag"

    rag_id     = Column(BigInteger, primary_key=True, autoincrement=True)
    user_id    = Column(BigInteger, ForeignKey("users.user_id", ondelete="CASCADE"), nullable=False)
    content    = Column(Text, nullable=False)
    category   = Column(String(50))
    created_at = Column(TIMESTAMP, nullable=False, server_default=func.current_timestamp())