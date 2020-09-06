import Layout from "@/layout";
export default {
  path: "/homework",
  component: Layout,
  redirect: "/homework/index",
  meta: { title: "作业管理", icon: "homework" },
  children: [
    {
      path: "display",
      name: "homeworkDisplay",
      component: () => import("@/views/homework/display"),
      meta: { title: "查看作业", icon: "table" }
    },
    {
      path: "add",
      name: "homeworkAdd",
      component: () => import("@/views/homework/add"),
      meta: { title: "添加作业", icon: "add", role:['admin', 'classAdmin'] }
    },
    // {
    //   path: "count",
    //   name: "homeworkCount",
    //   component: () => import("@/views/homework/count"),
    //   meta: { title: "统计作业", icon: "count" }
    // }
  ]
};
