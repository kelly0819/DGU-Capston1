import { useNavigate } from "react-router-dom";
import { PageHeader } from "../../components/common/PageHeader";
import { ProductThumbnail } from "../../components/common/ProductThumbnail";
import AppLayout from "../../layouts/AppLayout";

const similarProducts = [
  { name: "네오쿠션 매트", brand: "라네즈 · 만족도 96%", price: "28,000원", green: true },
  { name: "노세범 파우더", brand: "이니스프리 · 리오더 1위", price: "8,000원" },
  { name: "시카 톤업", brand: "닥터지 · 진정", price: "18,000원", green: true },
];

export function RecommendationResultPage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="min-h-screen overflow-hidden px-6 pb-6 pt-10">
        <PageHeader title="추천 결과 보고서" onBack={() => navigate(-1)} />

        <section className="relative mt-5 overflow-hidden rounded-2xl bg-gray-500 p-7 text-white">
          <div>
            <p className="text-body2 text-primary-300">AI 매칭 완료</p>
            <p className="mt-1 text-[42px] font-bold leading-none">95 <span className="text-body1">점</span></p>
            <p className="mt-3 text-body2 text-primary-300">인생템 확률 매칭</p>
          </div>
          <span className="absolute right-12 top-5 h-[92px] w-[92px] rounded-full bg-gray-400" />
          <span className="absolute right-7 top-16 h-[66px] w-[66px] rounded-full bg-primary-700" />
          <span className="absolute right-[78px] top-[70px] text-h1 text-white">✦</span>
        </section>

        <section className="mt-4 rounded-xl border border-primary-100 bg-primary-50 p-4">
          <p className="text-body2 text-primary-500">✦ AI가 분석한 추천 이유</p>
          <p className="mt-2 text-caption leading-6 text-gray-500">
            설정하신 3만원대 조건 하에, 지성 유저분의 파도한 유분을 통제하며 여름철에도 무너짐 없이 하루 종일 보송하게 밀착되는 완벽 매칭이에요
          </p>
        </section>

        <section className="mt-4">
          <h2 className="text-body1 text-gray-500">나와 비슷한 사람들은?</h2>
          <p className="text-caption text-gray-300">수부지·지성 타입의 실제 만족 제품</p>
          <div className="mt-3 flex gap-3 overflow-x-auto">
            {similarProducts.map((product) => (
              <article className="min-w-[154px] rounded-2xl border border-gray-200 bg-white p-3 text-center" key={product.name}>
                <ProductThumbnail className="h-[94px] bg-primary-50" green={product.green} size="wide" />
                <p className="mt-3 truncate text-body2 text-gray-500">{product.name}</p>
                <p className="truncate text-caption text-gray-300">{product.brand}</p>
                <p className="mt-1 text-caption text-primary-500">{product.price}</p>
              </article>
            ))}
          </div>
        </section>

        <section className="mt-4">
          <h2 className="text-body1 text-gray-500">유사한 대체 상품</h2>
          <div className="mt-3 flex items-center gap-4 rounded-xl border border-gray-200 bg-white p-4">
            <ProductThumbnail className="h-[60px] w-[60px] shrink-0" size="sm" />
            <div className="flex-1">
              <p className="text-body2 text-gray-500">올아워 파운데이션 매트</p>
              <p className="text-caption text-primary-500">성분 유사도 92% · 입생로랑</p>
              <p className="mt-1 text-body1 text-gray-500">32,000원</p>
            </div>
          </div>
        </section>

        <div className="mt-14 grid grid-cols-2 gap-3">
          <button className="h-[52px] rounded-xl border border-primary-100 bg-primary-50 text-body1 text-primary-500" type="button">
            결과 저장
          </button>
          <button className="h-[52px] rounded-xl border border-primary-100 bg-primary-50 text-body1 text-primary-500" type="button">
            공유하기
          </button>
        </div>

        <button
          className="mt-4 h-14 w-full rounded-xl bg-primary-500 text-body1 font-semibold text-white"
          onClick={() => navigate("/home")}
          type="button"
        >
          홈으로 돌아가기
        </button>
      </section>
    </AppLayout>
  );
}
