import router from "./router";
import store from "./store";
import { Message } from "element-ui";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import { getToken } from "@/utils/auth"; // get token from cookie
import getPageTitle from "@/utils/get-page-title";

NProgress.configure({ showSpinner: false }); // NProgress Configuration

const whiteList = ["/login", "/school/system"]; // no redirect whitelist

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
      next("/");
      NProgress.done();
    } else {
      next();
      NProgress.done();
    }
  } else {
    // 没有登录，判断是否白名单
    if (whiteList.indexOf(to.path) !== -1) {
      next();
      NProgress.done();
    } else {
      next(`/login?redirect=${to.path}`);
      NProgress.done();
    }
  }
  next();
  NProgress.done();
});

router.afterEach(() => {
  // finish progress bar
  NProgress.done();
});
