import { useNavigate } from "react-router-dom";
import AppLayout from "../../layouts/AppLayout";

export function OnboardingCompletePage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="flex min-h-screen flex-col px-6 pb-28 pt-[120px]">
        <div className="text-center">
          <div className="mx-auto flex h-40 w-40 items-center justify-center rounded-full bg-primary-50">
            <div className="flex h-[116px] w-[116px] items-center justify-center rounded-full bg-primary-100">
              <div className="flex h-[72px] w-[72px] items-center justify-center rounded-full bg-primary-500 text-white">
                <svg
                  aria-hidden="true"
                  className="h-11 w-11"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <path
                    d="m5 12 4 4L19 6"
                    stroke="currentColor"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2.5"
                  />
                </svg>
              </div>
            </div>
          </div>

          <h1 className="mt-6 text-h2 text-gray-500">준비 완료!</h1>
          <p className="mt-3 text-body2 text-gray-300">
            한지예님의 맞춤 설정이 완료됐어요
          </p>
        </div>

        <div className="mt-6 rounded-2xl border border-gray-200 bg-white p-4">
          <p className="text-body2 text-gray-300">내 피부 정보</p>
          <div className="mt-3 flex gap-2">
            {["봄웜", "지성 피부", "여드름"].map((item) => (
              <span
                className="rounded-full bg-primary-100 px-4 py-1 text-caption text-primary-500"
                key={item}
              >
                {item}
              </span>
            ))}
          </div>

          <div className="mt-4 grid gap-3 text-body2">
            <div className="flex justify-between border-t border-gray-100 pt-3">
              <span className="text-gray-300">등록한 제품</span>
              <strong className="text-gray-500">2개</strong>
            </div>
            <div className="flex justify-between border-t border-gray-100 pt-3">
              <span className="text-gray-300">가격 허용 폭</span>
              <strong className="text-gray-500">±10%</strong>
            </div>
            <div className="flex justify-between border-t border-gray-100 pt-3">
              <span className="text-gray-300">AI 분석 상태</span>
              <strong className="text-primary-500">분석 준비 완료</strong>
            </div>
          </div>
        </div>

        <div className="mt-4 rounded-xl bg-primary-50 p-4">
          <p className="text-body2 text-primary-500">✦ 이제 뭘 하면 될까요?</p>
          <p className="mt-1 text-caption text-gray-500">
            홈에서 제품을 검색하거나 AI 추천을 받아보세요
          </p>
        </div>

        <button
          className="mt-4 h-14 w-full rounded-xl bg-primary-500 text-body1 font-semibold text-white"
          onClick={() => navigate("/")}
          type="button"
        >
          BeautyMatch 시작하기
        </button>

        <p className="mt-5 text-center text-caption text-gray-300">
          설정은 MY 페이지에서 언제든 바꿀 수 있어요
        </p>
      </section>
    </AppLayout>
  );
}
