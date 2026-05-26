import { useNavigate } from "react-router-dom";
import AppLayout from "../../layouts/AppLayout";

const suggestions = [
  { name: "토리든 다이브인 세럼", desc: "레티놀 대신 히알루론산 기반", match: "85% 적합" },
  { name: "에스트라 아토베리어 크림", desc: "진정·장벽 강화 성분 함유", match: "82% 적합" },
];

export function SearchEmptyPage() {
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
          <h1 className="text-body1 text-gray-500">검색 결과</h1>
        </header>

        <div className="mt-5 flex h-12 items-center gap-3 rounded-xl border border-primary-500 bg-primary-50 px-4">
          <div className="h-5 w-5 rounded bg-primary-100" />
          <span className="flex-1 text-body2 text-gray-500">아이오페 레티놀 앰플</span>
          <button className="h-6 w-6 rounded-full bg-gray-200 text-gray-300" type="button">
            ×
          </button>
        </div>

        <div className="mt-10 text-center">
          <div className="mx-auto flex h-40 w-40 items-center justify-center rounded-full bg-gray-100">
            <div className="relative flex h-[90px] w-[90px] items-center justify-center rounded-full bg-gray-200">
              <span className="text-h1 text-gray-300">×</span>
              <span className="absolute bottom-5 right-4 h-9 w-2 rotate-[-45deg] rounded-full bg-gray-300" />
            </div>
          </div>
          <h2 className="mt-6 text-h3 text-gray-500">검색 결과가 없어요</h2>
          <p className="mt-2 text-body2 text-gray-300">
            "아이오페 레티놀 앰플"을
            <br />
            찾을 수 없었어요
          </p>
        </div>

        <div className="mt-10 rounded-xl border border-gray-200 bg-white p-4">
          <p className="text-body2 text-gray-500">이렇게 시도해보세요</p>
          <div className="my-3 h-px bg-gray-100" />
          <ul className="grid gap-2 text-caption text-gray-400">
            <li>● 검색어를 더 짧게 입력해보세요</li>
            <li>● 브랜드명만 입력해보세요 (예: 아이오페)</li>
            <li>● 사진으로 직접 등록해보세요</li>
          </ul>
          <button
            className="mt-2 text-caption text-primary-500"
            onClick={() => navigate("/onboarding/photo")}
            type="button"
          >
            사진으로 등록하기 →
          </button>
        </div>

        <div className="mt-5 flex items-center justify-between">
          <p className="text-body2 text-gray-500">이런 제품은 어떠세요?</p>
          <span className="text-caption text-gray-300">AI 추천</span>
        </div>

        <div className="mt-3 grid gap-3">
          {suggestions.map((product, index) => (
            <div
              className="flex items-center gap-4 rounded-xl border border-gray-200 bg-white p-3"
              key={product.name}
            >
              <div className="flex h-14 w-14 items-center justify-center rounded-xl bg-gray-100">
                <div
                  className={`h-9 w-9 rounded-md ${
                    index === 0 ? "bg-primary-100" : "bg-gray-200"
                  }`}
                />
              </div>
              <div>
                <p className="text-body2 text-gray-500">{product.name}</p>
                <p className="text-caption text-gray-300">{product.desc}</p>
                <span className="mt-2 inline-block rounded-full bg-primary-100 px-3 py-1 text-caption text-primary-500">
                  {product.match}
                </span>
              </div>
            </div>
          ))}
        </div>

        <button
          className="mt-auto h-[52px] w-full rounded-xl bg-gray-100 text-body1 text-gray-500"
          onClick={() => navigate("/onboarding/products")}
          type="button"
        >
          홈으로 돌아가기
        </button>
      </section>
    </AppLayout>
  );
}
