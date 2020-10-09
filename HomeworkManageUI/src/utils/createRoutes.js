import { asyncRoutes } from "@/router";
import Layout from "@/layout";
import HomeworkRoute from "@/views/homework/router";

function hasPermission(roles, route) {
  if (route.meta && route.meta.role) {
    return roles.some(role => route.meta.role.indexOf(role) >= 0);
  } else {
    return true;
  }
}

// 将菜单信息转成对应的路由信息 动态添加
export default function createRoutes(data) {
  const result = [];
  const children = [];
  console.log("createRoutes")
  const roles = JSON.parse(window.localStorage.getItem("user")).roles;
  if (roles) {
    data.forEach(name => {
      const route = asyncRoutes[name];
      if (route) {
        if (hasPermission(roles, route)) {
          if (route.children && route.children.length > 0) {
            route.children = route.children.filter(child => {
              if (hasPermission(roles, child)) {
                return child;
              }
              return false;
            });
          }
          result.push(route);
        }
      }
    });
  }

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
