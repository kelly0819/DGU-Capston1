type SearchFieldProps = {
  value: string;
  variant?: "filled" | "outlined";
  onClick?: () => void;
  onClear?: () => void;
};

export function SearchField({ onClear, onClick, value, variant = "filled" }: SearchFieldProps) {
  const colorClass =
    variant === "outlined"
      ? "border border-primary-500 bg-primary-50"
      : "bg-gray-100";

  return (
    <button
      className={`flex h-12 w-full items-center gap-3 rounded-xl px-4 text-left text-body2 ${colorClass}`}
      onClick={onClick}
      type="button"
    >
      <span className="h-5 w-5 shrink-0 rounded bg-primary-100" />
      <span className="min-w-0 flex-1 truncate text-gray-500">{value}</span>
      {onClear && (
        <span
          className="grid h-6 w-6 shrink-0 place-items-center rounded-full bg-gray-200 text-gray-300"
          onClick={(event) => {
            event.stopPropagation();
            onClear();
          }}
          role="button"
          tabIndex={0}
        >
          ×
        </span>
      )}
    </button>
  );
}
