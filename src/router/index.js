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
import SchoolRoute from "@/views/school/router";
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
  // {
  //   path: "/404",
  //   component: () => import("@/views/404"),
  //   hidden: true
  // },
  // // 404 page must be placed at the end !!!
  // { path: "*", redirect: "/404", hidden: true }
];

// 本地所有的页面 需要配合后台返回的数据生成页面
export const asyncRoutes = {
  homework: {
    path: "/homework",
    component: Layout,
    redirect: "/homework/display",
    meta: { title: "作业管理", icon: "homework" },
    children: [
      {
        path: "display",
        name: "homeworkDisplay",
        component: () => import("@/views/homework/display"),
        meta: { title: "查看作业", icon: "homework" }
      },
      {
        path: "add",
        name: "homeworkAdd",
        component: () => import("@/views/homework/add"),
        meta: { title: "添加作业", icon: "add" }
      },
      {
        path: "count",
        name: "homeworkCount",
        component: () => import("@/views/homework/count"),
        meta: { title: "统计作业", icon: "homework" }
      }
    ]
  }
  // userinfo: {
  //     path: 'userinfo',
  //     name: 'userinfo',
  //     meta: { title: '用户信息' },
  //     component: () => import('../views/UserInfo.vue'),
  // },
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
