import { useNavigate } from "react-router-dom";
import { Badge } from "../../components/common/Badge";
import { PageHeader } from "../../components/common/PageHeader";
import { ProductThumbnail } from "../../components/common/ProductThumbnail";
import AppLayout from "../../layouts/AppLayout";

const stores = [
  { name: "올리브영에서 구매가능", price: "28,600원", best: true, green: true },
  { name: "쿠팡에서 구매가능", price: "29,900원" },
  { name: "네이버쇼핑에서 확인하기", price: "31,200원" },
];

export function ProductDetailPage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="min-h-screen px-6 pb-28 pt-10">
        <PageHeader
          onBack={() => navigate(-1)}
          rightSlot={
            <button className="grid h-10 w-10 place-items-center rounded-xl bg-gray-100 text-h3 text-gray-300" type="button">
              ♡
            </button>
          }
        />

        <div className="relative mt-7 h-[210px] rounded-2xl bg-primary-300">
          <span className="absolute left-4 top-4 rounded-full bg-primary-500 px-3 py-2 text-caption text-white">
            98%의 적합성
          </span>
        </div>

        <p className="mt-3 text-caption text-gray-300">라네즈 쿠션 베이스 제품</p>
        <h1 className="mt-1 text-h2 text-gray-500">네오쿠션 21N 색상</h1>

        <div className="mt-7 flex items-center justify-between text-body2">
          <span className="text-gray-300">정가 정보</span>
          <span className="text-gray-300 line-through">42,000원</span>
        </div>

        <div className="mt-2 flex items-center justify-between rounded-xl bg-gray-500 px-5 py-4 text-white">
          <div className="flex items-center gap-4">
            <span className="text-primary-500">╋</span>
            <div>
              <p className="text-caption text-gray-300">최저가 검색 결과입니다.</p>
              <p className="text-h2">28,600원</p>
            </div>
          </div>
          <p className="text-caption">14,600원 절약 가능</p>
        </div>

        <h2 className="mt-5 text-body2 text-gray-500">최저가 구매처 안내</h2>
        <div className="mt-3 grid gap-3">
          {stores.map((store) => (
            <div
              className={`flex items-center gap-4 rounded-xl border bg-white p-4 ${
                store.best ? "border-primary-500" : "border-gray-200"
              }`}
              key={store.name}
            >
              <ProductThumbnail className="h-[60px] w-[60px] shrink-0" green={store.green} size="sm" />
              <div className="flex-1">
                <p className="text-body2 text-gray-500">{store.name}</p>
                {store.best && (
                  <Badge className="mt-1 inline-block">최저가</Badge>
                )}
              </div>
              <div className="text-right">
                <p className="text-body1 text-primary-500">{store.price}</p>
                <p className="text-caption text-gray-300">무료배송 제공</p>
                <button className="text-body2 text-primary-500" type="button">바로가기 ↗</button>
              </div>
            </div>
          ))}
        </div>

        <section className="mt-6 border-t border-gray-100 pt-5">
          <h2 className="text-body1 text-gray-500">주요 성분 소개</h2>
          <div className="mt-3 rounded-xl bg-primary-50 p-4">
            <div className="flex flex-wrap gap-2">
              {["나이아신아마이드", "히알루론산", "세라마이드"].map((item) => (
                <span
                  className="rounded-full border border-primary-500 px-3 py-1 text-caption text-primary-500"
                  key={item}
                >
                  {item}
                </span>
              ))}
            </div>
            <p className="mt-3 text-caption leading-6 text-gray-500">
              나이아신아마이드 - 피지 조절 및 미백 효과
              <br />
              히알루론산 - 보습 및 수분 공급 기능
              <br />
              세라마이드 - 피부 장벽 강화 효과
            </p>
          </div>
        </section>

        <section className="mt-6">
          <div className="flex justify-between">
            <h2 className="text-body1 text-gray-500">리뷰 요약</h2>
            <span className="text-caption text-gray-300">1,240개의 리뷰 기준</span>
          </div>
          <div className="mt-3 flex items-center justify-between rounded-xl border border-gray-200 bg-white p-4">
            <div>
              <span className="text-h1 text-gray-500">4.8 점</span>
              <span className="ml-3 text-[#FFB000]">★★★★★</span>
              <p className="text-caption text-gray-300">/ 5.0 점 지성 피부 사용자의 96% 만족도</p>
            </div>
            <div className="grid w-20 gap-2">
              <span className="h-1.5 rounded-full bg-primary-500" />
              <span className="h-1.5 w-2/3 rounded-full bg-primary-300" />
              <span className="h-1.5 w-1/3 rounded-full bg-primary-100" />
            </div>
          </div>
          <div className="mt-3 bg-primary-50 p-4">
            <p className="text-body2 text-primary-500">✦ AI 리뷰 요약</p>
            <p className="mt-2 text-caption leading-6 text-gray-500">
              "지성 피부 사용자들이 특히 만족하며, 하루 종일 밀착력이 유지된다는 후기가 많습니다. 단, 건성 피부는 가루날림이 있을 수 있습니다."
            </p>
          </div>
        </section>

        <div className="fixed bottom-0 left-1/2 flex h-[86px] w-full max-w-[430px] -translate-x-1/2 gap-3 bg-white px-6 py-4">
          <button className="h-12 w-14 rounded-xl border border-gray-300 text-h3 text-gray-300" type="button">♡</button>
          <button className="h-12 flex-1 rounded-xl bg-primary-500 text-body1 font-semibold text-white" type="button">
            최저가로 구매하기
          </button>
        </div>
      </section>
    </AppLayout>
  );
}
