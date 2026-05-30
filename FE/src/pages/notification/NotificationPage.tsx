import { useNavigate } from "react-router-dom";
import { PageHeader } from "../../components/common/PageHeader";
import AppLayout from "../../layouts/AppLayout";
import { todayNotifications, weekNotifications } from "../../mocks/notifications";

export function NotificationPage() {
  const navigate = useNavigate();

  return (
    <AppLayout>
      <section className="min-h-screen px-6 pb-20 pt-10">
        <PageHeader
          title="알림"
          onBack={() => navigate(-1)}
          rightSlot={
            <button className="text-body2 text-gray-300" type="button">
            모두 읽음
          </button>
          }
        />

        <h2 className="mt-7 text-body2 text-gray-500">오늘</h2>
        <div className="mt-2 grid gap-3">
          {todayNotifications.map((item) => (
            <NotificationCard key={item.title} {...item} />
          ))}
        </div>

        <h2 className="mt-4 text-body2 text-gray-500">이번 주</h2>
        <div className="mt-2 grid gap-3">
          {weekNotifications.map((item) => (
            <NotificationCard key={item.title} {...item} />
          ))}
        </div>

        <button className="mt-5 h-[52px] w-full rounded-xl bg-gray-100 text-body2 text-gray-300" type="button">
          더 이전 알림 없음
        </button>

        <div className="mt-12 text-center">
          <p className="text-caption text-gray-300">알림 설정 변경</p>
          <button className="mt-2 text-body2 text-primary-500" type="button">
            MY › 앱 설정 › 알림
          </button>
        </div>
      </section>
    </AppLayout>
  );
}

type NotificationCardProps = {
  title: string;
  desc: string;
  label: string;
  time: string;
  active?: boolean;
  icon?: string;
};

function NotificationCard({ active, desc, icon, label, time, title }: NotificationCardProps) {
  return (
    <article
      className={`relative rounded-xl border p-4 ${
        active ? "border-primary-500 bg-primary-50" : "border-gray-200 bg-white"
      }`}
    >
      {active && <span className="absolute right-4 top-4 h-3 w-3 rounded-full bg-primary-500" />}
      <div className="flex gap-4">
        <div
          className={`grid h-11 w-11 shrink-0 place-items-center rounded-full ${
            active ? "bg-primary-500 text-white" : "bg-gray-100 text-gray-300"
          }`}
        >
          {icon ?? (active ? "↓" : label === "AI 추천" ? "✦" : "%")}
        </div>
        <div className="min-w-0 flex-1">
          <p className={`text-caption ${active ? "text-primary-500" : "text-gray-300"}`}>{label}</p>
          <h3 className="mt-1 truncate text-body2 text-gray-500">{title}</h3>
          <p className="mt-1 truncate text-caption text-gray-400">{desc}</p>
          {active && (
            <button className="mt-2 rounded-md bg-primary-500 px-4 py-1 text-caption text-white" type="button">
              지금 구매
            </button>
          )}
        </div>
        <span className="self-end text-caption text-gray-300">{time}</span>
      </div>
    </article>
  );
}
