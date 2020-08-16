import Layout from "@/layout";
export default [
    {
        path: "/user",
        component: Layout,
        redirect: "/user/list",
        name: "用户管理",
        meta: { title: "用户管理", icon: "user" },
        children: [
            {
                path: "list",
                name: "List",
                component: () => import("@/views/user/index"),
                meta: { title: "用户列表", icon: "table" }
            },
            {
                path: "add",
                name: "Add",
                component: () => import("@/views/user/components/EditUser"),
                meta: { title: "添加用户", icon: "add" }
            }
        ]
    }
]