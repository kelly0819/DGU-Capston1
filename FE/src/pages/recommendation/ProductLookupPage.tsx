import { useNavigate } from "react-router-dom";
import { CameraMark } from "../../components/common/CameraMark";
import { PageHeader } from "../../components/common/PageHeader";
import AppLayout from "../../layouts/AppLayout";

export function ProductLookupPage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="flex min-h-screen flex-col px-6 pb-6 pt-10">
        <PageHeader title="제품 조회" onBack={() => navigate(-1)} />

        <div className="mt-7">
          <h1 className="text-h2 text-gray-500">
            어떤 방법으로
            <br />
            조회할까요?
          </h1>
          <p className="mt-2 text-body2 text-gray-300">
            제품을 찾아 내 피부와 비교해드려요
          </p>
        </div>

        <button
          className="mt-5 flex min-h-[280px] flex-col items-center justify-center rounded-2xl border border-dashed border-primary-100 bg-primary-50 px-8 text-center"
          onClick={() => navigate("/recommendation/extra-info")}
          type="button"
        >
          <CameraMark className="mb-5 h-[72px] w-[82px]" />
          <p className="text-body2 text-primary-500">사진으로 스캔</p>
          <p className="mt-2 text-caption text-primary-500">이미지 업로드 · AI 자동 인식</p>
          <p className="mt-2 text-caption text-gray-300">탭하여 카메라 열기</p>
        </button>

        <button className="mt-4 flex h-[56px] items-center gap-3 rounded-xl border border-gray-200 bg-white px-5 text-body2 text-gray-500" type="button">
          <span className="h-6 w-6 rounded-md bg-gray-200" />
          NFC 태그로 조회
        </button>

        <button className="mt-4 flex h-[68px] items-center gap-3 rounded-xl border border-gray-200 bg-white px-3" type="button">
          <div className="flex h-11 flex-1 items-center gap-3 rounded-xl bg-gray-100 px-4 text-body2 text-gray-300">
            <span className="h-6 w-6 rounded-md bg-gray-200" />
            브랜드명, 제품명 검색...
          </div>
        </button>

        <div className="mt-10 rounded-xl bg-primary-50 p-4">
          <p className="text-body2 text-primary-500">✦ BeautyMatch AI Tip</p>
          <p className="mt-1 text-caption leading-5 text-gray-500">
            목적과 예산을 함께 입력하면 피부 데이터와 결합해 최적의 가성비 제품을 찾아드려요
          </p>
        </div>

        <button
          className="mt-auto h-14 w-full rounded-xl bg-primary-500 text-body1 font-semibold text-white"
          onClick={() => navigate("/recommendation/extra-info")}
          type="button"
        >
          다음으로 →
        </button>
      </section>
    </AppLayout>
  );
}
