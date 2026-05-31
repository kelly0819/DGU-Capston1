import { useNavigate } from "react-router-dom";
import { BottomNav } from "../../components/common/BottomNav";
import { ProductThumbnail } from "../../components/common/ProductThumbnail";
import AppLayout from "../../layouts/AppLayout";
import { myMenuItems } from "../../mocks/user";

export function MyPage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="min-h-screen px-6 pb-28 pt-12">
        <header className="flex items-center justify-between">
          <h1 className="text-h3 text-gray-500">My</h1>
          <button className="grid h-10 w-10 place-items-center rounded-full bg-gray-100" type="button">
            <span className="h-3 w-3 rounded-sm bg-gray-200" />
          </button>
        </header>

        <section className="mt-4 rounded-2xl bg-gray-500 p-5 text-white">
          <div className="flex items-start justify-between">
            <div className="flex items-center gap-4">
              <div className="grid h-16 w-16 place-items-center rounded-full bg-primary-700">
                <div className="grid h-11 w-11 place-items-center rounded-full bg-primary-500 text-h4">한</div>
              </div>
              <div>
                <h2 className="text-h4">한지예</h2>
                <p className="mt-1 text-caption text-primary-300">봄웜 · 지성 피부 · 여드름</p>
                <button className="mt-3 rounded-full bg-white/20 px-5 py-1 text-caption" type="button">
                  프로필 수정
                </button>
              </div>
            </div>
            <span className="rounded-full bg-primary-500 px-3 py-1 text-caption">업데이트됨</span>
          </div>
        </section>

        <div className="mt-4 grid grid-cols-3 gap-3">
          {[
            ["12", "최근 검색"],
            ["6", "찜 목록"],
            ["3", "내 리뷰"],
          ].map(([value, label], index) => (
            <div
              className={`rounded-xl border p-4 text-center ${index === 2 ? "border-primary-100 bg-primary-50" : "border-gray-200 bg-white"}`}
              key={label}
            >
              <p className={`text-h2 ${index === 2 ? "text-primary-500" : "text-gray-500"}`}>{value}</p>
              <p className={`mt-1 text-caption ${index === 2 ? "text-primary-500" : "text-gray-300"}`}>{label}</p>
            </div>
          ))}
        </div>

        <div className="mt-4 grid gap-3">
          {myMenuItems.map((item) => (
            <button
              className="flex items-center gap-4 rounded-xl border border-gray-200 bg-white p-4 text-left"
              key={item.title}
              onClick={() => navigate(item.path)}
              type="button"
            >
              <ProductThumbnail className="h-11 w-11 shrink-0 bg-primary-50" green size="sm" />
              <div className="flex-1">
                <p className="text-body2 text-gray-500">{item.title}</p>
                {item.desc && <p className="mt-1 text-caption text-gray-300">{item.desc}</p>}
              </div>
              <span className="grid h-8 w-8 place-items-center rounded-lg bg-gray-100 text-gray-300">›</span>
            </button>
          ))}
        </div>

        <button className="mt-5 w-full py-4 text-body2 text-gray-300" type="button">
          로그아웃
        </button>

        <BottomNav />
      </section>
    </AppLayout>
  );
}
