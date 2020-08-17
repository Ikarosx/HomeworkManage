import Layout from "@/layout";
export default {
  path: "/",
  component: Layout,
  redirect: "/index",
  children: [
    {
      path: "index",
      name: "index",
      component: () => import("@/views/dashboard/index"),
      meta: { title: "首页", icon: "dashboard" }
    }
  ]
};
