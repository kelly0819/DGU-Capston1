"""product_reader read-only 조회 테스트. Supabase 호출은 모킹."""
from unittest.mock import MagicMock, patch

from db.product_reader import get_product_meta, get_products_meta


def _row(pid="p1", name="네오쿠션", brand="LANEIGE"):
    return {
        "id": pid,
        "name": name,
        "brand": brand,
        "category": "base",
        "image_url": "https://img/x.jpg",
        "original_price": 30000,
    }


class TestProductReader:
    @patch("db.product_reader.get_supabase")
    def test_get_product_meta_found(self, mock_get_sb):
        sb = MagicMock()
        sb.table.return_value.select.return_value.eq.return_value.limit.return_value.execute.return_value.data = [_row()]
        mock_get_sb.return_value = sb

        meta = get_product_meta("p1")
        assert meta is not None
        assert meta["id"] == "p1"
        assert meta["name"] == "네오쿠션"
        assert meta["price"] == 30000

    @patch("db.product_reader.get_supabase")
    def test_get_product_meta_not_found(self, mock_get_sb):
        sb = MagicMock()
        sb.table.return_value.select.return_value.eq.return_value.limit.return_value.execute.return_value.data = []
        mock_get_sb.return_value = sb

        assert get_product_meta("nope") is None

    @patch("db.product_reader.get_supabase")
    def test_get_products_meta_batch(self, mock_get_sb):
        sb = MagicMock()
        sb.table.return_value.select.return_value.in_.return_value.execute.return_value.data = [
            _row("p1", "쿠션A", "브랜드A"),
            _row("p2", "쿠션B", "브랜드B"),
        ]
        mock_get_sb.return_value = sb

        metas = get_products_meta(["p1", "p2"])
        assert set(metas.keys()) == {"p1", "p2"}
        assert metas["p1"]["name"] == "쿠션A"

    def test_get_products_meta_empty_input(self):
        # 빈 입력은 DB 호출 없이 빈 딕셔너리
        assert get_products_meta([]) == {}