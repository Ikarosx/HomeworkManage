import Layout from "@/layout"
export default [{
    path: "/school",
    component: Layout,
    redirect: "/school/list",
    name: "学校管理",
    meta: { title: "学校管理", icon: "school" },
    children: [{
        path: "list",
        name: "schoolList",
        component: () => import("@/views/school"),
        meta: { title: "学校列表", icon: "table" }
    },{
        path: "add",
        name: "schoolAdd",
        component: () => import("@/views/school/components/EditSchool"),
        meta: { title: "添加学校", icon: "add" }
    },{
        path: "system",
        name: "System",
        component: () => import("@/views/school/system"),
        meta: { title: "校园网系统", icon: "system" }
    }]
}]