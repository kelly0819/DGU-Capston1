import { api } from "../lib/api";

interface ApiResponse<T> {
  success: boolean;
  data: T;
}

export interface RecognizeResult {
  productId: string;
  name: string;
  brand: string;
  imageUrl?: string | null;
}

export interface ProductSearchItem {
  id: string;
  name: string;
  brand: string;
  category: string;
  imageUrl?: string | null;
  originalPrice?: number | null;
}

export const recognizeProduct = (type: "IMAGE" | "TEXT", data: string) =>
  api.post<ApiResponse<RecognizeResult>>("/products/recognize", { type, data });

export const searchProducts = (keyword: string) =>
  api.get<ApiResponse<{ products: ProductSearchItem[] }>>(
    `/products/search?keyword=${encodeURIComponent(keyword)}`,
  );
