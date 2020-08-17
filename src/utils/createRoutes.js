import { asyncRoutes } from "@/router";
import Layout from "@/layout";
import HomeworkRoute from "@/views/homework/router";
// 将菜单信息转成对应的路由信息 动态添加
export default function createRoutes(data) {
  const result = [];
  const children = [];

  data.forEach(item => {
    if (asyncRoutes[item.name]) result.push(asyncRoutes[item.name]);
  });

  result.push({
    path: "/404",
    component: () => import("@/views/404"),
    hidden: true
  });
  // 404 page must be placed at the end !!!
  result.push({ path: "*", redirect: "/404", hidden: true });

  return result;
}

function generateRoutes(children, item) {
  console.log(item);
  //   if (item.name) {
  if (asyncRoutes[item.name]) children.push(asyncRoutes[item.name]);
  //   } else if (item.children) {
  //     item.children.forEach(e => {
  //       generateRoutes(children, e);
  //     });
  //   }
}
