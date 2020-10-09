import Layout from "@/layout"
export default [{
    path: "/shop",
    component: Layout,
    redirect: "/good/list",
    name: "shop",
    meta: { title: "购物管理", icon: "shop" },
    children: [{
        path: "good/list",
        name: "goodList",
        component: () => import("@/views/shop/good"),
        meta: { title: "商品列表", icon: "table" }
    },{
        path: "good/add",
        name: "goodAdd",
        component: () => import("@/views/shop/components/EditGood"),
        meta: { title: "添加商品", icon: "add" }
    },{
        path: "category/list",
        name: "categoryList",
        component: () => import("@/views/shop/category"),
        meta: { title: "分类列表", icon: "table" }
    },{
        path: "category/add",
        name: "categoryAdd",
        component: () => import("@/views/shop/components/EditCategory"),
        meta: { title: "添加分类", icon: "add" }
    }]
}]