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
]);
