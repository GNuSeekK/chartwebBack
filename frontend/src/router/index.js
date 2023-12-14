import { createRouter, createWebHistory } from "vue-router";
import MainPage from "@/components/MainPage.vue";

const router = createRouter({
  history: createWebHistory(""),
  routes: [
    {
      path: "/",
      name: "Home",
      component: MainPage
    }
  ],
});

export default router;
