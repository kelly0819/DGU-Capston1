-- Migration: 001_enable_pgvector
-- 목적: pgvector 확장 활성화
-- 의존: 없음

CREATE EXTENSION IF NOT EXISTS vector;