import router from "./router";
import store from "./store";
import createRoutes from "@/utils/createRoutes";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import getPageTitle from "@/utils/get-page-title";
import { systemConfig } from "@/../config/system";
import http from "@/api/public";

const apiUrl = systemConfig.apiUrl;

NProgress.configure({ showSpinner: false }); // NProgress Configuration

const whiteList = ["/login", "/school/system"]; // no redirect whitelist

// 是否有菜单数据
let hasMenus = false;
router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start();
  // set page title
  document.title = getPageTitle(to.meta.title);
  // determine whether the user has logged in
  const user = window.localStorage.getItem("user");
  if (user != null) {
    if (to.path === "/login") {
      // 已经登陆后访问login直接跳转到根目录
      next({ path: "/" });
      NProgress.done();
    } else if (hasMenus) {
      next();
      NProgress.done();
    } else {
      // 添加菜单
      // store.dispatch("GenerateRoutes").then(() => {
      //   // 生成可访问的路由表
      //   router.addRoutes(store.getters.addRouters); // 动态添加可访问路由表
      //   next({ ...to, replace: true }); // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
      // }).catch((err) => {
      //   console.log(err);
      // })

      try {
        var data = await http.requestQuickGet(apiUrl + "/user/menus");
      } catch (error) {
        next({ path: "/" });
        NProgress.done();
      }
      // 解决权限需要刷新才显示的问题
      // store.commit("user/SET_ROLES", data.data.roles);
      let user = JSON.parse(window.localStorage.getItem("user"));
      console.log(data)
      user.roles = data.data.roles;
      store.dispatch("user/setUserInfo", user);
      window.localStorage.setItem("user", JSON.stringify(user));
      const routes = createRoutes(data.data.menus);
      router.addRoutes(routes);
      const orgRoutes = router.options.routes;
      const newRoutes = orgRoutes.concat(routes);
      store.commit("user/SET_ROUTES", newRoutes);
      hasMenus = true;
      next({ ...to, replace: true }); // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
      // next({ path: to.path || '/' });
    }
  } else {
    hasMenus = false;
    // 没有登录，判断是否白名单
    if (whiteList.indexOf(to.path) !== -1) {
      next();
      NProgress.done();
    } else {
      next(`/login?redirect=${to.path}`);
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  // finish progress bar
  NProgress.done();
});
