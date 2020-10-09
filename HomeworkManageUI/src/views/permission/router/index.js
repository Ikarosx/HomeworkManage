import Layout from "@/layout";
export default [
  {
    path: "/permission",
    component: Layout,
    name: "permission",
    meta: { title: "权限管理", icon: "permission" },
    children: [
      {
        path: "role/list",
        name: "roleList",
        component: () => import("@/views/permission/role"),
        meta: { title: "角色列表", icon: "table" }
      },
      {
        path: "role/add",
        name: "roleAdd",
        component: () => import("@/views/permission/components/EditRole"),
        meta: { title: "添加角色", icon: "add" }
      },
      {
        path: "menu/list",
        name: "menuList",
        component: () => import("@/views/permission/menu"),
        meta: { title: "权限列表", icon: "table" }
      },
      {
        path: "menu/add",
        name: "menuAdd",
        component: () => import("@/views/permission/components/EditMenu"),
        meta: { title: "添加权限", icon: "add" }
      }
    ]
  }
];
