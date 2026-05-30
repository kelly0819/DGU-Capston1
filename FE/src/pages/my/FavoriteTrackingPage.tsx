import { useNavigate } from "react-router-dom";
import { BottomNav } from "../../components/common/BottomNav";
import { PageHeader } from "../../components/common/PageHeader";
import { ProductThumbnail } from "../../components/common/ProductThumbnail";
import { Toggle } from "../../components/common/Toggle";
import AppLayout from "../../layouts/AppLayout";
import { favoriteTrackingProducts } from "../../mocks/priceTracking";

export function FavoriteTrackingPage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="min-h-screen px-6 pb-28 pt-10">
        <PageHeader title="관심 제품" onBack={() => navigate(-1)} rightSlot={<button className="text-body2 text-primary-500" type="button">정렬</button>} />

        <section className="mt-5 rounded-2xl bg-gray-500 p-6 text-white">
          <div className="grid grid-cols-2 divide-x divide-gray-400/40">
            <div>
              <p className="text-caption">추적 중인 제품</p>
              <p className="mt-1 text-[34px] leading-none">4 <span className="text-body1">개</span></p>
            </div>
            <div className="pl-5">
              <p className="text-caption">이번 달 절약 금액</p>
              <p className="mt-1 text-[34px] leading-none">6,400 <span className="text-body1">원</span></p>
            </div>
          </div>
        </section>

        <h2 className="mt-5 text-body1 text-gray-500">지금 살 때예요!</h2>
        <p className="text-caption text-gray-300">설정한 목표가에 도달한 제품이에요</p>
        <button className="mt-3 flex w-full items-center gap-3 rounded-xl border border-primary-500 bg-white p-3 text-left" onClick={() => navigate("/price-tracking/laneige-neo-cushion")} type="button">
          <ProductThumbnail className="h-[60px] w-[60px] shrink-0 bg-primary-50" green size="sm" />
          <div className="flex-1">
            <p className="text-body2 text-gray-500">라네즈 네오쿠션 21N</p>
            <p className="text-caption text-gray-300">목표가 29,000원 달성!</p>
            <p className="mt-1 text-body2 text-primary-500"><span className="line-through text-gray-300">38,000원</span> 28,600원</p>
          </div>
          <span className="rounded-xl bg-primary-500 px-6 py-3 text-body2 text-white">지금 구매</span>
        </button>

        <h2 className="mt-5 text-body1 text-gray-500">추적 중</h2>
        <div className="mt-3 grid gap-3">
          {favoriteTrackingProducts.map(([name, brand, current, target]) => (
            <article className="flex items-center gap-4 rounded-xl border border-gray-200 bg-white p-4" key={name}>
              <ProductThumbnail className="h-[60px] w-[60px] shrink-0 bg-primary-50" green size="sm" />
              <div className="flex-1">
                <p className="text-body2 text-gray-500">{name}</p>
                <p className="text-caption text-gray-300">{brand}</p>
                <div className="mt-4 grid grid-cols-2 gap-3">
                  <div>
                    <p className="text-caption text-gray-300">현재가</p>
                    <p className="text-h4 text-gray-500">{current}</p>
                    <p className="text-caption text-gray-300">최저가 기록 7,500원</p>
                  </div>
                  <div>
                    <p className="text-caption text-gray-300">목표가</p>
                    <p className="rounded-full bg-primary-100 px-5 py-2 text-center text-body2 text-gray-500">{target}</p>
                  </div>
                </div>
              </div>
            </article>
          ))}
        </div>

        <section className="mt-5 rounded-xl border border-gray-200 bg-white">
          <h2 className="px-4 pt-4 text-body1 text-gray-500">알림 설정</h2>
          {["목표가 도달 시 알림", "주간 가격 리포트"].map((item, index) => (
            <div className="flex items-center gap-3 border-b border-gray-100 p-4 last:border-b-0" key={item}>
              <div className="flex-1">
                <p className="text-body2 text-gray-500">{item}</p>
                <p className="text-caption text-gray-300">{index === 0 ? "설정한 금액 이하가 되면 푸시 알림을 보내요" : "매주 월요일 가격 변동 요약을 보내드려요"}</p>
              </div>
              <Toggle checked={index === 0} />
            </div>
          ))}
        </section>

        <button className="mt-5 h-[58px] w-full rounded-xl border border-dashed border-primary-100 bg-primary-50 text-body1 text-primary-500" onClick={() => navigate("/price-tracking/add")} type="button">
          +<br />제품 추가하기
        </button>

        <BottomNav />
      </section>
    </AppLayout>
  );
}
