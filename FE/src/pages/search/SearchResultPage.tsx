import { useNavigate } from "react-router-dom";
import { BottomNav } from "../../components/common/BottomNav";
import { PageHeader } from "../../components/common/PageHeader";
import { ProductListItem } from "../../components/common/ProductListItem";
import { SearchField } from "../../components/common/SearchField";
import AppLayout from "../../layouts/AppLayout";

const filters = ["전체", "스킨케어", "베이스", "선케어", "클렌징"];

const products = [
  { name: "라네즈 네오쿠션 21N", meta: "라네즈 · 쿠션 · 베이스", price: "38,000원", match: "85% 적합", best: true, green: true },
  { name: "미샤 매직 쿠션", meta: "미샤 · 쿠션 · 베이스", price: "14,000원", match: "91% 적합" },
  { name: "클리오 킬 커버 쿠션", meta: "클리오 · 쿠션 · 베이스", price: "26,000원", match: "87% 적합" },
  { name: "맥 스튜디오 픽스", meta: "MAC · 파우더 파운데이션", price: "52,000원", match: "85% 적합" },
];

export function SearchResultPage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="flex min-h-screen flex-col px-6 pb-28 pt-10">
        <PageHeader title="검색 결과" onBack={() => navigate(-1)} />

        <div className="mt-5">
          <SearchField value="여름 가벼운 쿠션 추천해줘" variant="outlined" onClear={() => undefined} />
        </div>

        <p className="mt-4 text-caption text-gray-300">총 12개의 결과</p>
        <div className="mt-3 flex gap-2 overflow-x-auto">
          {filters.map((filter, index) => (
            <button
              className={`h-10 rounded-full px-4 text-caption ${
                index === 0 ? "bg-primary-500 text-white" : "bg-gray-100 text-gray-500"
              }`}
              key={filter}
              type="button"
            >
              {filter}
            </button>
          ))}
        </div>

        <div className="mt-4 grid gap-4">
          {products.map((product, index) => (
            <ProductListItem
              key={product.name}
              best={product.best}
              green={product.green}
              match={product.match}
              meta={product.meta}
              name={product.name}
              onClick={() => navigate("/product/laneige-neo-cushion")}
              price={product.price}
              selected={index === 0}
            />
          ))}
        </div>

        <div className="mt-5 rounded-xl bg-primary-50 p-4">
          <p className="text-body2 text-primary-500">✦ AI 추천 기준</p>
          <p className="mt-1 text-caption text-gray-500">
            지성 피부 + 여름철 조건으로 정렬했어요
          </p>
        </div>

        <BottomNav />
      </section>
    </AppLayout>
  );
}
