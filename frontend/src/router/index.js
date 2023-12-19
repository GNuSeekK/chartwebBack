import { createRouter, createWebHistory } from "vue-router";
import MainPage from "@/components/MainPage.vue";
import ChartPage from "@/components/ChartPage.vue";
import NewsPage from "@/components/NewsPage.vue";
import MyPage from "@/components/MyPage.vue";
import MyChart from "@/components/MyChart.vue";
import StockToday from "@/components/StockToday.vue";

const router = createRouter({
  history: createWebHistory(""),
  routes: [
    {
      path: "/",
      name: "Home",
      component: MainPage
    },
    {
      path: "/chart",
      name: "Chart",
      component: ChartPage
    },
    {
      path: "/news",
      name: "News",
      component: NewsPage
    },
    {
      name: "MyPage",
      path: "/mypage",
      component: MyPage
    },
    {
      name: "MyChart",
      path: "/mychart",
      component: MyChart
    },
    {
      path: "/stock-today",
      name: "StockToday",
      component: StockToday
    }

  ],
});

export default router;
