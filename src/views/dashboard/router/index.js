import Layout from "@/layout";
export default [{
    path: "/",
    component: Layout,
    redirect: "/dashboard",
    children: [{
      path: "dashboard",
      name: "Dashboard",
      component: () => import("@/views/dashboard/index"),
      meta: { title: "Dashboard", icon: "dashboard" }
    }]
}]