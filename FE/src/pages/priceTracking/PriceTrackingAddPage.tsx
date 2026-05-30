import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { PageHeader } from "../../components/common/PageHeader";
import { PrimaryButton } from "../../components/common/PrimaryButton";
import { ProductMiniCard } from "../../components/common/ProductMiniCard";
import { ProductThumbnail } from "../../components/common/ProductThumbnail";
import { Toggle } from "../../components/common/Toggle";
import AppLayout from "../../layouts/AppLayout";
import { modalTargetPrices, pickedTrackingProducts, quickTargetPrices } from "../../mocks/priceTracking";

export function PriceTrackingAddPage() {
  const navigate = useNavigate();
  const [modalOpen, setModalOpen] = useState(false);

  return (
    <AppLayout>
      <section className="min-h-screen px-6 pb-6 pt-10">
        <PageHeader title="가격 추적 추가" onBack={() => navigate(-1)} />

        <div className="mt-7">
          <h1 className="text-h2 text-gray-500">
            어떤 제품을
            <br />
            추적할까요?
          </h1>
          <p className="mt-2 text-body2 text-gray-300">Gemini가 실시간으로 가격을 모니터링해요</p>
        </div>

        <div className="mt-6 flex items-center justify-between">
          <h2 className="text-body2 text-gray-500">찜 목록에서 선택</h2>
          <button className="text-caption text-primary-500" type="button">전체보기</button>
        </div>
        <div className="mt-3 flex gap-2 overflow-x-auto">
          {pickedTrackingProducts.map((product) => (
            <ProductMiniCard
              key={product.name}
              green
              name={product.name}
              price={product.price}
              selected={product.active}
              subtitle={product.brand}
            />
          ))}
          <button className="min-w-[82px] rounded-xl bg-gray-100 text-center text-gray-300" type="button">
            <span className="block text-h3">+</span>
            <span className="text-caption">더보기</span>
          </button>
        </div>

        <Divider />

        <h2 className="text-body2 text-gray-500">직접 검색해서 추가</h2>
        <button className="mt-3 flex h-12 w-full items-center gap-3 rounded-xl bg-gray-100 px-4 text-body2 text-gray-300" type="button">
          <span className="h-5 w-5 rounded bg-gray-200" />
          <span className="flex-1 text-left">브랜드명, 제품명 검색...</span>
          <span className="text-h3 text-gray-500">⌕</span>
        </button>

        <Divider />

        <h2 className="text-body2 text-gray-500">선택한 제품</h2>
        <div className="mt-3 flex items-center gap-3 rounded-xl border border-primary-500 bg-white p-3">
          <ProductThumbnail className="h-14 w-14 shrink-0 bg-primary-50" green size="sm" />
          <div className="flex-1">
            <p className="text-body2 text-gray-500">라네즈 네오쿠션 21N</p>
            <p className="text-caption text-gray-300">라네즈 · 현재가 38,000원</p>
            <p className="mt-1 text-caption text-primary-500">Gemini 최저가 28,600원</p>
          </div>
          <button className="h-7 w-7 rounded-full bg-gray-100 text-gray-300" type="button">×</button>
        </div>

        <h2 className="mt-5 text-body2 text-gray-500">목표가 설정</h2>
        <button
          className="mt-3 flex h-[60px] w-full items-center justify-between rounded-xl border border-primary-500 px-3"
          onClick={() => setModalOpen(true)}
          type="button"
        >
          <span className="grid h-9 w-9 place-items-center rounded-lg bg-gray-100 text-h3">−</span>
          <strong className="text-h2 text-gray-500">29,000원</strong>
          <span className="grid h-9 w-9 place-items-center rounded-lg bg-gray-100 text-h3">＋</span>
        </button>
        <p className="mt-2 text-caption text-gray-200">빠른 설정</p>
        <div className="mt-2 grid grid-cols-4 gap-2">
          {quickTargetPrices.map((item, index) => (
            <button className={`h-10 rounded-lg whitespace-pre-line text-[10px] ${index === 1 ? "bg-primary-500 text-white" : "bg-gray-100 text-gray-400"}`} key={item} type="button">
              {item}
            </button>
          ))}
        </div>

        <div className="mt-3 flex items-center gap-3 rounded-xl border border-gray-200 p-4">
          <div className="flex-1">
            <p className="text-body2 text-gray-500">목표가 도달 시 푸시 알림</p>
            <p className="text-caption text-gray-300">설정한 금액 이하가 되면 바로 알려드려요</p>
          </div>
          <Toggle checked />
        </div>

        <PrimaryButton className="mt-5">추적 시작하기</PrimaryButton>
      </section>

      {modalOpen && <TargetPriceModal onClose={() => setModalOpen(false)} />}
    </AppLayout>
  );
}

function TargetPriceModal({ onClose }: { onClose: () => void }) {
  return (
    <div className="fixed inset-0 z-50 flex items-end justify-center bg-black/60">
      <div className="w-full max-w-[430px] rounded-t-3xl bg-white px-6 pb-9 pt-4">
        <div className="mx-auto mb-6 h-1 w-12 rounded-full bg-gray-200" />
        <p className="ml-auto w-fit rounded-full bg-primary-50 px-5 py-2 text-caption text-primary-500">현재 목표 29,000원</p>
        <div className="mt-3 flex h-[76px] items-center justify-between rounded-2xl bg-gray-100 px-3">
          <button className="grid h-12 w-12 place-items-center rounded-xl bg-white text-h3 shadow-sm" type="button">−</button>
          <strong className="text-h2 text-gray-500">29,000원</strong>
          <button className="grid h-12 w-12 place-items-center rounded-xl bg-white text-h3 shadow-sm" type="button">＋</button>
        </div>
        <p className="mt-2 text-center text-caption text-gray-300">1,000원 단위로 조정돼요</p>
        <div className="mt-3 grid grid-cols-4 gap-2">
          {modalTargetPrices.map((item, index) => (
            <button className={`h-10 rounded-lg whitespace-pre-line text-[10px] ${index === 2 ? "bg-primary-500 text-white" : "bg-gray-100 text-gray-400"}`} key={item} type="button">
              {item}
            </button>
          ))}
        </div>
        <PrimaryButton className="mt-4" onClick={onClose}>
          목표가 저장하기
        </PrimaryButton>
      </div>
    </div>
  );
}

function Divider() {
  return <div className="my-5 h-px bg-gray-100" />;
}
