"""
리뷰 텍스트를 임베딩하기 적절한 청크로 분할.

- 너무 짧은 리뷰(< 10자)는 신호가 약해 제외
- 너무 긴 리뷰(> 300자)는 문장 단위로 분할
- 이모지·URL·반복 자음("ㅋㅋㅋ") 등 노이즈 제거

review_embeddings 테이블에 저장될 review_text와 embedding을 만들 때 사용.
"""
import re
from typing import List

MIN_REVIEW_LEN = 10
MAX_REVIEW_LEN = 300

# 이모지 패턴 (유니코드 4바이트 영역)
_EMOJI_PATTERN = re.compile(
    "["
    "\U0001F600-\U0001F64F"
    "\U0001F300-\U0001F5FF"
    "\U0001F680-\U0001F6FF"
    "\U0001F1E0-\U0001F1FF"
    "\U00002702-\U000027B0"
    "\U000024C2-\U0001F251"
    "]+",
    flags=re.UNICODE,
)
_URL_PATTERN = re.compile(r"https?://\S+|www\.\S+")
_REPEAT_HANGUL = re.compile(r"([ㄱ-ㅎㅏ-ㅣ])\1{2,}")  # 3회 이상 반복 자모
_SENTENCE_SPLIT = re.compile(r"(?<=[.!?])\s+")
_MULTI_SPACE = re.compile(r"\s+")


def clean_noise(text: str) -> str:
    """이모지, URL, 반복 자음 제거 후 공백 정리."""
    text = _EMOJI_PATTERN.sub("", text)
    text = _URL_PATTERN.sub("", text)
    text = _REPEAT_HANGUL.sub(r"\1", text)
    text = _MULTI_SPACE.sub(" ", text)
    return text.strip()


def _split_by_sentence(text: str, max_len: int) -> List[str]:
    """문장 단위로 분할 후 max_len 넘는 청크는 강제로 자름."""
    sentences = _SENTENCE_SPLIT.split(text)
    chunks: List[str] = []
    current = ""
    for s in sentences:
        if not s:
            continue
        if len(current) + len(s) + 1 <= max_len:
            current = f"{current} {s}".strip() if current else s
        else:
            if current:
                chunks.append(current)
            if len(s) <= max_len:
                current = s
            else:
                # 한 문장이 max_len 초과 → 강제 분할
                for i in range(0, len(s), max_len):
                    chunks.append(s[i:i + max_len])
                current = ""
    if current:
        chunks.append(current)
    return chunks


def chunk_review(text: str) -> List[str]:
    """
    리뷰 텍스트를 임베딩 가능한 청크 리스트로 변환.

    Returns:
        - 노이즈 제거 후 < 10자면 빈 리스트
        - <= 300자면 단일 청크
        - > 300자면 문장 단위로 분할
    """
    cleaned = clean_noise(text)
    if len(cleaned) < MIN_REVIEW_LEN:
        return []
    if len(cleaned) <= MAX_REVIEW_LEN:
        return [cleaned]
    return _split_by_sentence(cleaned, MAX_REVIEW_LEN)