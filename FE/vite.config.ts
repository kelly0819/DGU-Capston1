import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      "/auth": {
        target: "http://localhost:8080",
        changeOrigin: true,
        bypass: (req) => {
          // GET 요청은 OAuth 콜백 등 브라우저 네비게이션 → React가 처리
          // POST/PATCH/DELETE 등 API 호출만 Spring으로 프록시
          if (req.method === "GET") return req.url;
        },
      },
      "/products": "http://localhost:8080",
      "/recommendations": "http://localhost:8080",
      "/users": "http://localhost:8080",
      "/notifications": "http://localhost:8080",
      "/onboarding": "http://localhost:8080",
      "/wishlist": "http://localhost:8080",
      "/price-tracking": "http://localhost:8080",
      "/ai": {
        target: "http://localhost:8001",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/ai/, ""),
      },
    },
  },
});
