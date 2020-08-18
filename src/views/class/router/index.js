import Layout from "@/layout";
export default {
  path: "/class",
  component: Layout,
  redirect: "/class/waiting",
  meta: { title: "班级管理", icon: "class", role:['admin', 'classAdmin'] },
  children: [
    {
      path: "waiting",
      name: "classWaitingPeople",
      component: () => import("@/views/class/waiting"),
      meta: { title: "申请列表", icon: "table" }
    },
    {
      path: "display",
      name: "classList",
      component: () => import("@/views/class/display"),
      meta: { title: "班级列表", icon: "table",role:['admin'] }
    }
  ]
};
