import { useNavigate } from "react-router-dom";
import AppLayout from "../../layouts/AppLayout";

export function PhotoRegisterPage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="flex min-h-screen flex-col px-6 pb-7 pt-10">
        <header className="relative flex h-6 items-center justify-center">
          <button
            className="absolute left-0 text-h3 text-gray-500"
            onClick={() => navigate(-1)}
            type="button"
            aria-label="뒤로가기"
          >
            ←
          </button>
          <h1 className="text-body1 text-gray-500">사진으로 등록</h1>
        </header>

        <div className="mt-7">
          <h2 className="text-h2 text-gray-500">
            화장품 정면을
            <br />
            비춰주세요
          </h2>
          <p className="mt-2 text-body2 text-gray-300">
            AI가 브랜드와 제품명을 자동으로 인식해요
          </p>
        </div>

        <div className="mt-5 flex min-h-[280px] flex-col items-center justify-center rounded-2xl border border-dashed border-primary-100 bg-primary-50 px-8 text-center">
          <div className="mb-5 flex h-[72px] w-[82px] items-center justify-center rounded-xl bg-primary-100">
            <div className="flex h-10 w-12 items-center justify-center rounded-lg bg-primary-300">
              <div className="h-5 w-5 rounded-full border-4 border-primary-700" />
            </div>
          </div>
          <p className="text-body2 text-primary-500">제품을 프레임 안에 맞춰주세요</p>
          <p className="mt-2 text-caption text-primary-500">
            글자가 선명하게 보일수록 정확도가 높아요
          </p>
          <p className="mt-2 text-caption text-primary-500">
            빛 반사를 피하고 평평한 면에 올려주세요
          </p>
          <p className="mt-5 text-caption text-primary-500">인식률 93% ↑</p>
        </div>

        <div className="mt-4 flex items-center gap-3 rounded-xl border border-primary-100 bg-white p-3">
          <div className="flex h-14 w-14 items-center justify-center rounded-xl bg-primary-50">
            <div className="h-9 w-9 rounded-md bg-primary-300" />
          </div>
          <div className="flex-1">
            <span className="rounded-md bg-primary-100 px-3 py-1 text-caption text-primary-500">
              인식 중...
            </span>
            <div className="mt-2 h-2 rounded-full bg-gray-100">
              <div className="h-full w-7/10 rounded-full bg-gray-200" />
            </div>
            <div className="mt-2 h-2 w-1/2 rounded-full bg-gray-100" />
          </div>
          <button className="text-caption text-primary-500" type="button">
            추가
          </button>
        </div>

        <div className="mt-4 rounded-xl bg-gray-100 p-4">
          <p className="text-body2 text-gray-500">잘 인식되지 않나요?</p>
          <p className="mt-1 text-caption text-gray-300">
            앨범에서 기존 사진을 불러올 수도 있어요
          </p>
        </div>

        <div className="mt-4 grid grid-cols-2 gap-4">
          <button
            className="h-[52px] rounded-xl bg-gray-500 text-body2 text-white"
            type="button"
          >
            촬영하기
          </button>
          <button
            className="h-[52px] rounded-xl bg-gray-100 text-body2 text-gray-500"
            type="button"
          >
            앨범에서 선택
          </button>
        </div>
      </section>
    </AppLayout>
  );
}
