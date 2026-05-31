import { api } from "../lib/api";

interface ApiResponse<T> {
  success: boolean;
  data: T;
}

// ── 프로필 ──────────────────────────────────────────────────────────

export interface ProfileUpdateResponse {
  id: string;
  name: string;
  gender: string | null;
  profileImageUrl: string | null;
}

export const updateProfile = (
  name: string | null,
  gender: string | null,
  image: File | null,
) => {
  const form = new FormData();
  if (name) form.append("name", name);
  if (gender) form.append("gender", gender);
  if (image) form.append("image", image);
  return api.patchForm<ApiResponse<ProfileUpdateResponse>>("/users/profile", form);
};

// ── 피부 프로필 ──────────────────────────────────────────────────────

// 프론트 label → 백엔드 enum 매핑
const PERSONAL_COLOR_MAP: Record<string, string> = {
  "spring-warm": "SPRING_WARM",
  "summer-cool": "SUMMER_COOL",
  "autumn-warm": "AUTUMN_MUTE",
  "winter-cool": "WINTER_COOL",
  "unknown":     "UNKNOWN",
};

const SKIN_TYPE_MAP: Record<string, string> = {
  "건성":  "DRY",
  "중성":  "NORMAL",
  "지성":  "OILY",
  "복합성": "COMBINATION",
  "수부지": "DEHYDRATED_OILY",
};

const CONCERN_MAP: Record<string, string> = {
  "민감성":      "SENSITIVITY",
  "여드름":      "ACNE",
  "아토피":      "ATOPY",
  "미백/잡티":   "WHITENING",
  "피지/블랙헤드": "SEBUM",
  "모공":        "PORE",
  "다크서클":    "DARK_CIRCLE",
  "홍조":        "REDNESS",
  "각질":        "TEXTURE",
  "주름/탄력":   "WRINKLE",
  // "속건조", "해당 없음" → 백엔드 enum 없으므로 제외
};

export const saveOnboardingProducts = (productIds: string[]) =>
  api.post<ApiResponse<number>>("/onboarding/favorites/items/batch", { productIds });

export const completeOnboarding = () =>
  api.patch<ApiResponse<unknown>>("/onboarding");

export const updateSkinProfile = (
  personalColor: string,
  skinType: string,
  concerns: string[],
) =>
  api.patch<ApiResponse<unknown>>("/users/me/skin-profile", {
    personalColor: PERSONAL_COLOR_MAP[personalColor] ?? null,
    skinType:      SKIN_TYPE_MAP[skinType] ?? null,
    skinConcerns:  concerns
      .map((c) => CONCERN_MAP[c])
      .filter(Boolean),
  });
