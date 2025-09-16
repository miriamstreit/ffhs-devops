import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/stores/user";
import { parseJwt } from "./helpers/jwtHelper";

import Home from "./pages/TheHome.vue";
import Inventory from "./pages/TheInventory.vue";
import Calendar from "./pages/TheCalendar.vue";
import CanBrew from "./pages/CanBrew.vue";
import Login from "./pages/TheLogin.vue";

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      component: Home,
    },
    {
      path: "/calendar",
      component: Calendar,
    },
    {
      path: "/inventory",
      component: Inventory,
    },
    {
      path: "/canbrew",
      component: CanBrew,
    },
    {
      path: "/login",
      component: Login,
    },
  ],
});

// Routguard
// Checks befor every route, if the user is allowed
router.beforeEach((to, from, next) => {
  const publicPages = ["/login"];
  const authRequired = !publicPages.includes(to.path);

  const userStore = useUserStore();
  const loggedIn = userStore.getUser?.token;

  if (authRequired && !loggedIn) {
    return next("/login");
  }

  if (loggedIn) {
    const jwtPayload = parseJwt(userStore.getUser?.token);

    if (jwtPayload.exp < Date.now() / 1000) {
      userStore.logout();
      next("/login");
    }
  }

  next();
});
