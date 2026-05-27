import { useNavigate } from "react-router-dom";
import AppLayout from "../../layouts/AppLayout";

const products = [
  { name: "그린티 세럼", meta: "이니스프리 · 스킨케어 · 세럼", added: true },
  { name: "그린티 씨드 크림", meta: "이니스프리 · 스킨케어 · 세럼" },
  { name: "화산송이 모공 폼", meta: "이니스프리 · 클렌징" },
];

const recentProducts = [
  { name: "그린티 세럼", brand: "이니스프리", green: true },
  { name: "네오쿠션 21N", brand: "라네즈" },
];

export function ProductSearchSetupPage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="flex min-h-screen flex-col px-6 pb-6 pt-10">
        <header className="relative flex h-6 items-center justify-center">
          <button
            className="absolute left-0 text-h3 text-gray-500"
            onClick={() => navigate(-1)}
            type="button"
            aria-label="뒤로가기"
          >
            ←
          </button>
          <h1 className="text-body1 text-gray-500">제품 검색</h1>
        </header>

        <div className="mt-5 flex h-12 items-center gap-3 rounded-xl border border-primary-500 bg-primary-50 px-4">
          <div className="h-5 w-5 rounded bg-primary-100" />
          <span className="flex-1 text-body2 text-gray-500">이니스프리</span>
          <button
            className="h-6 w-6 rounded-full bg-gray-200 text-gray-300"
            onClick={() => navigate("/onboarding/search-empty")}
            type="button"
            aria-label="검색어 지우기"
          >
            ×
          </button>
        </div>

        <div className="mt-4 flex items-center justify-between">
          <p className="text-body2 text-gray-500">검색 결과 3개</p>
          <button className="rounded-full bg-gray-100 px-5 py-2 text-caption text-gray-400" type="button">
            필터∨
          </button>
        </div>

        <div className="mt-3 grid gap-3">
          {products.map((product) => (
            <div
              className="flex items-center gap-4 rounded-xl border border-gray-200 bg-white p-3"
              key={product.name}
            >
              <div className="flex h-14 w-14 items-center justify-center rounded-xl bg-primary-50">
                <div
                  className={`h-9 w-9 rounded-md ${
                    product.added ? "bg-primary-100" : "bg-gray-200"
                  }`}
                />
              </div>
              <div className="min-w-0 flex-1">
                <p className="truncate text-body2 text-gray-500">{product.name}</p>
                <p className="truncate text-caption text-gray-300">{product.meta}</p>
                <div className="mt-2 h-1.5 w-20 rounded-full bg-gray-100" />
              </div>
              <button
                className={`h-8 rounded-xl px-4 text-caption ${
                  product.added
                    ? "bg-primary-100 text-primary-500"
                    : "bg-primary-500 text-white"
                }`}
                type="button"
              >
                {product.added ? "추가됨 ✓" : "추가"}
              </button>
            </div>
          ))}
        </div>

        <p className="mt-5 text-body2 text-gray-500">최근 추가한 제품</p>
        <div className="mt-3 flex gap-3">
          {recentProducts.map((product) => (
            <div
              className="w-[102px] rounded-xl border border-gray-200 bg-white p-2 text-center"
              key={product.name}
            >
              <div className="flex h-[62px] items-center justify-center rounded-lg bg-gray-100">
                <div
                  className={`h-11 w-14 rounded-md ${
                    product.green ? "bg-primary-100" : "bg-gray-200"
                  }`}
                />
              </div>
              <p className="mt-2 truncate text-caption text-gray-500">{product.name}</p>
              <p className="truncate text-caption text-gray-300">{product.brand}</p>
            </div>
          ))}
        </div>

        <button
          className="mt-auto h-14 w-full rounded-xl bg-primary-500 text-body1 font-semibold text-white"
          onClick={() => navigate("/onboarding/complete")}
          type="button"
        >
          검색 완료
        </button>
      </section>
    </AppLayout>
  );
}
