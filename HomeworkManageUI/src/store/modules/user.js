import { getToken, setToken, removeToken } from "@/utils/auth";
import { router } from "@/router";
import Layout from "@/layout"
import http from "@/api/public";
import { systemConfig } from "@/../config/system";
const apiUrl = systemConfig.apiUrl;

const getDefaultState = () => {
  return {
    name: "",
    avatar: "https://ikaros-picture.oss-cn-shenzhen.aliyuncs.com/typora/Ikaros/timg.jpg",
    isAdmin: false,
    isClassAdmin: false,
    classId: "",
    routes: [],
    roles: window.localStorage.getItem("user") && JSON.parse(window.localStorage.getItem("user")).roles
  };
};

const state = getDefaultState();

const mutations = {
  RESET_STATE: state => {
    Object.assign(state, getDefaultState());
  },
  SET_NAME: (state, name) => {
    state.name = name;
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar;
  },
  SET_ISADMIN: (state, isAdmin) => {
    state.isAdmin = isAdmin;
  },
  SET_ISCLASSADMIN: (state, isClassAdmin) => {
    state.isClassAdmin = isClassAdmin;
  },
  SET_CLASSID: (state, classId) => {
    state.classId = classId;
  },
  SET_ROUTES: (state, routes) => {
    state.routes = routes;
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
};

const actions = {
  // user login
  login({ commit }, userInfo) {
    return http.requestPost(apiUrl + "/user/login", userInfo);
  },
  register({ commit }, userInfo) {
    return http.requestPost(apiUrl + "/user", userInfo);
  },

  setUserInfo({commit, state}, user){
    commit("SET_ROLES", user.roles);
    commit("SET_NAME", user.nickname);
    commit("SET_CLASSID", user.classId);
    commit("SET_ISADMIN", user.type == 1);
    commit("SET_ISCLASSADMIN", user.roles.indexOf('classAdmin') > -1);
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token)
        .then(response => {
          const { data } = response;
          if (!data) {
            reject("Verification failed, please Login again.");
          }
          const { name, avatar } = data;
          commit("SET_NAME", name);
          commit("SET_AVATAR", avatar);
          resolve(data);
        })
        .catch(error => {
          reject(error);
        });
    });
  },

  // user logout
  logout({ commit, state }) {
    return http.requestQuickGet(apiUrl + "/user/logout");
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
