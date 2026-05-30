import { createBrowserRouter } from "react-router-dom";
import { LoginPage } from "./pages/auth/LoginPage";
import { ProfileSetupPage } from "./pages/onboarding/ProfileSetupPage";
import { PreferenceSetupPage } from "./pages/onboarding/PreferenceSetupPage";
import { KnownProductSetupPage } from "./pages/onboarding/KnownProductSetupPage";
import { PhotoRegisterPage } from "./pages/onboarding/PhotoRegisterPage";
import { ProductSearchSetupPage } from "./pages/onboarding/ProductSearchSetupPage";
import { SearchEmptyPage } from "./pages/onboarding/SearchEmptyPage";
import { OnboardingCompletePage } from "./pages/onboarding/OnboardingCompletePage";
import { HomePage } from "./pages/home/HomePage";
import { SearchResultPage } from "./pages/search/SearchResultPage";
import { ProductDetailPage } from "./pages/product/ProductDetailPage";
import { NotificationPage } from "./pages/notification/NotificationPage";
import { ProductLookupPage } from "./pages/recommendation/ProductLookupPage";
import { ExtraInfoPage } from "./pages/recommendation/ExtraInfoPage";
import { RecommendationLoadingPage } from "./pages/recommendation/RecommendationLoadingPage";
import { RecommendationResultPage } from "./pages/recommendation/RecommendationResultPage";
import { MyPage } from "./pages/my/MyPage";
import { SkinInfoEditPage } from "./pages/my/SkinInfoEditPage";
import { FavoriteTrackingPage } from "./pages/my/FavoriteTrackingPage";
import { FavoriteProductsPage } from "./pages/my/FavoriteProductsPage";
import { PriceTrackingAddPage } from "./pages/priceTracking/PriceTrackingAddPage";
import { PriceHistoryPage } from "./pages/priceTracking/PriceHistoryPage";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <LoginPage />,
  },
  {
    path: "/onboarding/profile",
    element: <ProfileSetupPage />,
  },
  {
    path: "/onboarding/preference",
    element: <PreferenceSetupPage />,
  },
  {
    path: "/onboarding/products",
    element: <KnownProductSetupPage />,
  },
  {
    path: "/onboarding/photo",
    element: <PhotoRegisterPage />,
  },
  {
    path: "/onboarding/product-search",
    element: <ProductSearchSetupPage />,
  },
  {
    path: "/onboarding/search-empty",
    element: <SearchEmptyPage />,
  },
  {
    path: "/onboarding/complete",
    element: <OnboardingCompletePage />,
  },
  {
    path: "/home",
    element: <HomePage />,
  },
  {
    path: "/search",
    element: <SearchResultPage />,
  },
  {
    path: "/product/:productId",
    element: <ProductDetailPage />,
  },
  {
    path: "/notifications",
    element: <NotificationPage />,
  },
  {
    path: "/recommendation/lookup",
    element: <ProductLookupPage />,
  },
  {
    path: "/recommendation/extra-info",
    element: <ExtraInfoPage />,
  },
  {
    path: "/recommendation/loading",
    element: <RecommendationLoadingPage />,
  },
  {
    path: "/recommendation/result",
    element: <RecommendationResultPage />,
  },
  {
    path: "/my",
    element: <MyPage />,
  },
  {
    path: "/my/skin",
    element: <SkinInfoEditPage />,
  },
  {
    path: "/favorites",
    element: <FavoriteTrackingPage />,
  },
  {
    path: "/favorites/products",
    element: <FavoriteProductsPage />,
  },
  {
    path: "/price-tracking/add",
    element: <PriceTrackingAddPage />,
  },
  {
    path: "/price-tracking/:productId",
    element: <PriceHistoryPage />,
  },
]);
