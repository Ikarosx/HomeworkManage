import Vue from "vue";
import Router from "vue-router";
Vue.use(Router);

/* Layout */
import Layout from "@/layout";

let bdRoutes = [];
let concat = router => {
  bdRoutes = bdRoutes.concat(router);
};
import UserRoute from "@/views/user/router";
import ClassRoute from "@/views/class/router";
import LoginRoute from "@/views/login/router";
import DashboardRoute from "@/views/dashboard/router";
import PermissionRoute from "@/views/permission/router";
import ShopRoute from "@/views/shop/router";
import HomeworkRoute from "@/views/homework/router";


// concat(LoginRoute);
// concat(DashboardRoute);

// concat(UserRoute);
// concat(PermissionRoute);
// concat(SchoolRoute);
// concat(ShopRoute);

// concat(HomeworkRoute);

export const constantRoutes = [LoginRoute,DashboardRoute
  // 404 放在动态生成里
];

// 本地所有的页面 需要配合后台返回的数据生成页面
export const asyncRoutes = {
  homework: HomeworkRoute,
  class: ClassRoute
};

concat(constantRoutes);
const createRouter = () =>
  new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }), 
    routes: bdRoutes
  });

const router = createRouter();
// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
