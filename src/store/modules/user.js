import { getToken, setToken, removeToken } from "@/utils/auth";
import { router } from "@/router";
import Layout from "@/layout"
import http from "@/api/public";
import { systemConfig } from "@/../config/system";
const apiUrl = systemConfig.apiUrl;

const getDefaultState = () => {
  return {
    token: getToken(),
    name: "",
    avatar: "https://ikaros-picture.oss-cn-shenzhen.aliyuncs.com/typora/Ikaros/timg.jpg",
    isAdmin: false,
    classId: "",
    routes: []
  };
};

const state = getDefaultState();

const mutations = {
  RESET_STATE: state => {
    Object.assign(state, getDefaultState());
  },
  SET_TOKEN: (state, token) => {
    state.token = token;
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
  SET_CLASSID: (state, classId) => {
    state.classId = classId;
  },
  SET_ROUTES: (state, routes) => {
    state.routes = routes;
  }
};

const actions = {
  // user login
  login({ commit }, userInfo) {
    return http.requestPost(apiUrl + "/user/login", userInfo);
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

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken(); // must remove  token  first
      commit("RESET_STATE");
      resolve();
    });
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
