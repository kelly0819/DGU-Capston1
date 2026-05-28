import AppLayout from "../../layouts/AppLayout";

const agentTools = [
  {
    name: "VLM",
    action: "제품 이미지 인식",
    detail: "브랜드, 제품명, 카테고리 추출",
    call: "call_input_agent()",
  },
  {
    name: "Score",
    action: "매칭 점수 계산",
    detail: "피부 타입과 성분 적합도 비교",
    call: "call_score_agent()",
  },
  {
    name: "Alternative",
    action: "대체상품 탐색",
    detail: "가격대와 유사 성분 후보 검색",
    call: "call_alternative_agent()",
  },
  {
    name: "Report",
    action: "추천 리포트 생성",
    detail: "추천 이유와 구매 후보 정리",
    call: "final_answer",
  },
];

export function RecommendationLoadingPage() {
  return (
    <AppLayout>
      <section className="flex min-h-screen flex-col px-6 pb-10 pt-10">
        <p className="text-h4 text-gray-500">BeautyMatch</p>

        <div className="mt-10 text-center">
          <LoadingOrb />
          <h1 className="mt-8 text-h2 text-gray-500">
            최적의 매칭을
            <br />
            찾는 중이에요
          </h1>
          <p className="mt-2 text-body2 text-gray-300">잠깐이면 충분해요</p>
        </div>

        <AgentToolFlow />

        <div className="mt-4 rounded-xl bg-primary-50 p-4">
          <p className="text-body2 text-primary-500">✦ 잠깐, 피부 팁!</p>
          <p className="mt-1 text-caption leading-5 text-gray-500">
            민감한 날엔 진정 성분 제품을 우선 추천해드려요
          </p>
        </div>

        <div className="mt-auto h-1.5 overflow-hidden rounded-full bg-gray-200">
          <div className="recommendation-progress h-full rounded-full bg-primary-500" />
        </div>
      </section>
    </AppLayout>
  );
}

function AgentToolFlow() {
  return (
    <section className="mt-6 rounded-2xl border border-gray-200 bg-white p-4">
      <div className="mb-3 flex items-center justify-between">
        <div>
          <p className="text-caption text-primary-500">Action Model</p>
          <h2 className="text-body2 text-gray-500">도구를 선택해 추천을 수행 중</h2>
        </div>
        <span className="agent-thinking-dot" />
      </div>

      <ol className="grid gap-2">
        {agentTools.map((tool, index) => (
          <li className="agent-tool-step" key={tool.name} style={{ animationDelay: `${index * 1.15}s` }}>
            <div className="agent-tool-icon">{index + 1}</div>
            <div className="min-w-0 flex-1">
              <div className="flex items-center gap-2">
                <strong className="text-caption text-gray-500">{tool.name}</strong>
                <span className="truncate text-[11px] text-primary-500">{tool.call}</span>
              </div>
              <p className="mt-0.5 text-caption text-gray-500">{tool.action}</p>
              <p className="truncate text-[11px] text-gray-300">{tool.detail}</p>
            </div>
            <span className="agent-tool-status">대기</span>
          </li>
        ))}
      </ol>
    </section>
  );
}

function LoadingOrb() {
  return (
    <div className="match-loader mx-auto" aria-label="AI 추천 분석 중">
      <div className="match-loader-glow" />
      <div className="match-loader-scene">
        <div className="match-card-stack">
          <div className="match-card card-back" />
          <div className="match-card card-mid" />
          <div className="match-card card-front">
            <span className="match-card-line line-1" />
            <span className="match-card-line line-2" />
            <span className="match-card-dot" />
          </div>
        </div>
      </div>
      <span className="pop-bubble pop-1" />
      <span className="pop-bubble pop-2" />
      <span className="pop-bubble pop-3" />
      <span className="pop-bubble pop-4" />
      <span className="pop-bubble pop-5" />
    </div>
  );
}
