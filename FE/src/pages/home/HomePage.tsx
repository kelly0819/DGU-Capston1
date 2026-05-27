import { useNavigate } from "react-router-dom";
import { Badge } from "../../components/common/Badge";
import { ProductThumbnail } from "../../components/common/ProductThumbnail";
import { SearchField } from "../../components/common/SearchField";
import AppLayout from "../../layouts/AppLayout";

const recommendedProducts = [
  { name: "글래스 레이어 쿠션", match: "98% 적합", price: "32,000원", green: true },
  { name: "에어리 피팅 파데", match: "92% 적합", price: "28,000원" },
  { name: "톤업 선 쿠션", match: "90% 적합", price: "24,000원", green: true },
];

const recentProducts = ["그린티 세럼", "시카 앰플", "진정 크림", "시카 토너"];

export function HomePage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="min-h-screen overflow-hidden px-6 pb-24 pt-10">
        <header className="flex items-start justify-between">
          <div>
            <h1 className="text-h3 text-gray-500">BeautyMatch</h1>
            <p className="mt-1 text-body2 text-gray-300">안녕하세요, 한지예님 👋</p>
          </div>
          <button
            className="relative h-11 w-11 rounded-full bg-gray-100"
            onClick={() => navigate("/notifications")}
            type="button"
            aria-label="알림"
          >
            <span className="absolute right-0 top-0 h-3 w-3 rounded-full bg-primary-500" />
            <span className="absolute left-1/2 top-1/2 h-3 w-3 -translate-x-1/2 -translate-y-1/2 rounded-sm bg-gray-200" />
          </button>
        </header>

        <div className="mt-5">
          <SearchField value="여름 가벼운 쿠션 추천해줘" onClick={() => navigate("/search")} />
        </div>

        <button
          className="mt-4 w-full rounded-xl border border-primary-300 bg-primary-50 p-4 text-left"
          onClick={() => navigate("/notifications")}
          type="button"
        >
          <p className="text-caption font-semibold text-primary-500">가격 추적 알림</p>
          <p className="mt-1 text-caption text-gray-500">
            라네즈 네오쿠션 21N이 목표가(29,000원)에 도달했어요
          </p>
        </button>

        <section className="mt-4 overflow-hidden rounded-2xl bg-gray-500 text-white">
          <div className="grid grid-cols-[1fr_46%]">
            <div className="p-5">
              <p className="text-caption text-primary-300">오늘의 추천 ✦</p>
              <h2 className="mt-2 text-h3">
                내 피부에 딱 맞는
                <br />
                제품을 찾았어요
              </h2>
              <button
                className="mt-5 rounded-full bg-primary-500 px-7 py-2 text-caption font-semibold"
                onClick={() => navigate("/search")}
                type="button"
              >
                확인하기 →
              </button>
            </div>
            <div className="relative min-h-[118px] rounded-l-2xl bg-primary-700">
              <span className="absolute left-16 top-7 h-16 w-16 rounded-full bg-primary-100/20" />
              <span className="absolute right-6 top-12 h-12 w-12 rounded-full bg-primary-300/30" />
              <span className="absolute bottom-7 left-12 h-6 w-6 rounded-full bg-primary-300/40" />
            </div>
          </div>
        </section>

        <section className="mt-4">
          <div className="mb-2 flex items-center justify-between">
            <h2 className="text-body1 text-gray-500">최근 검색 기반 추천</h2>
            <button className="text-body2 text-primary-500" type="button">
              더보기
            </button>
          </div>
          <div className="flex gap-3 overflow-x-auto pb-1">
            {recommendedProducts.map((product) => (
              <button
                className="min-w-[154px] rounded-2xl border border-gray-200 bg-white p-3 text-left"
                key={product.name}
                onClick={() => navigate("/product/laneige-neo-cushion")}
                type="button"
              >
                <ProductThumbnail className="h-[94px] bg-primary-50" green={product.green} size="wide" />
                <p className="mt-3 truncate text-body2 text-gray-500">{product.name}</p>
                <div className="mt-2 flex items-center justify-between">
                  <Badge>{product.match}</Badge>
                  <span className="text-caption text-gray-300">{product.price}</span>
                </div>
              </button>
            ))}
          </div>
        </section>

        <section className="mt-4 rounded-xl border border-primary-100 bg-primary-50 p-4">
          <p className="text-body2 text-primary-500">✦ AI 분석</p>
          <p className="mt-1 text-caption text-gray-500">
            가벼운 제형 선호도 + 여름철 지성 고민 기반으로 피지 조절 강화 제품을 우선 추천했어요
          </p>
        </section>

        <section className="mt-4">
          <h2 className="text-body1 text-gray-500">최근 본 상품</h2>
          <div className="mt-3 flex gap-3 overflow-x-auto">
            {recentProducts.map((product) => (
              <div
                className="min-w-[96px] rounded-xl border border-gray-200 bg-white p-2 text-center"
                key={product}
              >
                <div className="h-[70px] rounded-lg bg-gray-100" />
                <p className="mt-2 truncate text-caption text-gray-500">{product}</p>
              </div>
            ))}
          </div>
        </section>

        <div className="fixed bottom-6 left-1/2 flex w-[342px] max-w-[calc(100%-48px)] -translate-x-1/2 items-center gap-3">
          <button className="flex flex-1 items-center gap-3 rounded-full bg-gray-500 px-4 py-3 text-left" type="button">
            <span className="grid h-8 w-8 place-items-center rounded-full bg-gray-400 text-primary-300">＋</span>
            <span className="flex-1">
              <span className="block text-body2 text-white">가격 추적 중</span>
              <span className="block text-caption text-primary-300">4개 제품 · 1개 목표가 달성</span>
            </span>
            <span className="text-primary-300">›</span>
          </button>
          <button className="h-16 w-16 rounded-full bg-primary-500 text-caption font-semibold text-white" type="button">
            가격
            <br />
            추적
          </button>
        </div>
      </section>
    </AppLayout>
  );
}
