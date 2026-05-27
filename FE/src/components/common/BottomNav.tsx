export function BottomNav() {
  return (
    <nav className="fixed bottom-0 left-1/2 grid h-[90px] w-full max-w-[430px] -translate-x-1/2 grid-cols-3 items-center bg-white px-6">
      <button
        className="mx-auto grid h-[52px] w-[52px] place-items-center rounded-xl bg-primary-50 text-caption text-gray-300"
        type="button"
      >
        ♧
        <span>HOME</span>
      </button>
      <button
        className="mx-auto grid h-[52px] w-[52px] place-items-center rounded-full bg-primary-500 text-white"
        type="button"
      >
        ⊙
      </button>
      <button
        className="mx-auto grid h-[52px] w-[52px] place-items-center rounded-xl bg-primary-50 text-caption text-gray-300"
        type="button"
      >
        ○
        <span>MY</span>
      </button>
    </nav>
  );
}
