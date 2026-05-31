import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { PageHeader } from "../../components/common/PageHeader";
import { PrimaryButton } from "../../components/common/PrimaryButton";
import AppLayout from "../../layouts/AppLayout";
import { personalColors, skinConcerns, skinNotes, skinTypes } from "../../mocks/user";

export function SkinInfoEditPage() {
  const navigate = useNavigate();
  const [skinType, setSkinType] = useState("지성");
  const [color, setColor] = useState("웜톤\n봄 웜");
  const [selected, setSelected] = useState(["민감성", "여드름"]);

  function toggleConcern(concern: string) {
    setSelected((current) =>
      current.includes(concern) ? current.filter((item) => item !== concern) : [...current, concern],
    );
  }

  return (
    <AppLayout>
      <section className="flex min-h-screen flex-col px-6 pb-6 pt-10">
        <PageHeader
          title="추가 정보 입력"
          onBack={() => navigate(-1)}
          rightSlot={<button className="text-body2 text-primary-500" type="button">저장</button>}
        />

        <div className="mt-5">
          <h1 className="text-h2 text-gray-500">피부 정보</h1>
          <p className="mt-2 text-body2 text-gray-300">맞춤 서비스를 위해 반드시 입력해주세요</p>
        </div>

        <section className="mt-5">
          <h2 className="mb-2 text-body2 text-gray-500">피부 타입</h2>
          <div className="flex gap-3">
            {skinTypes.map((type) => (
              <button
                className={`h-10 rounded-xl px-5 text-body2 ${skinType === type ? "bg-primary-300 text-white" : "bg-gray-100 text-gray-500"}`}
                key={type}
                onClick={() => setSkinType(type)}
                type="button"
              >
                {type}
              </button>
            ))}
          </div>
        </section>

        <section className="mt-5">
          <h2 className="mb-2 text-body2 text-gray-500">퍼스널 컬러</h2>
          <div className="grid grid-cols-2 gap-3">
            {personalColors.map((item) => (
              <button
                className={`h-[52px] rounded-xl whitespace-pre-line text-body2 ${item === "잘 모르겠어요" ? "col-span-2" : ""} ${
                  color === item ? "bg-gray-500 text-white" : "bg-gray-100 text-gray-300"
                }`}
                key={item}
                onClick={() => setColor(item)}
                type="button"
              >
                {item}
              </button>
            ))}
          </div>
        </section>

        <section className="mt-5">
          <h2 className="mb-2 text-body2 text-gray-500">피부 고민 (복수선택)</h2>
          <div className="flex flex-wrap gap-3">
            {skinConcerns.map((concern) => (
              <button
                className={`h-9 rounded-full px-4 text-body2 ${
                  selected.includes(concern) ? "bg-primary-300 text-white" : "bg-gray-100 text-gray-500"
                }`}
                key={concern}
                onClick={() => toggleConcern(concern)}
                type="button"
              >
                {concern}
              </button>
            ))}
          </div>
        </section>

        <section className="mt-5">
          <h2 className="mb-2 text-body2 text-gray-500">특이사항</h2>
          {skinNotes.map((note) => (
            <div className="mb-3 rounded-xl border border-gray-200 bg-white p-4" key={note}>
              <p className="text-body2 text-gray-500">{note}</p>
              <div className="mt-2 h-2 w-3/4 rounded-full bg-gray-100" />
              <div className="mt-2 h-2 w-1/2 rounded-full bg-gray-100" />
            </div>
          ))}
          <button className="w-full py-2 text-body2 text-primary-500" type="button">+ 추가하기</button>
        </section>

        <PrimaryButton className="mt-auto">정보 저장하기</PrimaryButton>
      </section>
    </AppLayout>
  );
}
