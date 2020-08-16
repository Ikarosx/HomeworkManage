require("es6-promise").polyfill();
import axios from "axios";
import { Message } from "element-ui"
axios.defaults.withCredentials = true; //跨域
axios.defaults.timeout = 5000;
axios.defaults.headers.post["Content-Type"] =
  "application/x-www-form-urlencoded";

// import utils from '../../common/utils'
// if (utils.getJwt()) {
//   axios.defaults.headers['Authorization'] = 'Bearer ' + utils.getJwt()
// }
// request interceptor
axios.interceptors.request.use(
  config => {
    // do something before request is sent

    // let each request carry token
    // ['X-Token'] is a custom headers key
    // please modify it according to the actual situation
    return config;
  },
  error => {
    // do something with request error
    console.log(error); // for debug
    return Promise.reject(error);
  }
);

// response interceptor
axios.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data;

    // if the custom code is not 20000, it is judged as an error.
    if (res.code !== 10000) {
      Message({
        message: res.message || "Error",
        type: "error",
        duration: 5 * 1000
      });

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 11002) {
        // to re-login
        // 清除localStorage
        window.localStorage.removeItem("user");
        window.location.href = "/login";
      }
      return Promise.reject(new Error(res.message || "Error"));
    } else {
      return response;
    }
  },
  error => {
    console.log("err" + error); // for debug
    Message({
      message: error.message,
      type: "error",
      duration: 5 * 1000
    });
    return Promise.reject(error);
  }
);

export default {
  //get请求
  requestGet(url, params = {}) {
    return new Promise((resolve, reject) => {
      axios
        .get(url, params)
        .then(res => {
          resolve(res.data);
        })
        .catch(error => {
          reject(error);
        });
    });
  },
  //get请求不带参数
  requestQuickGet(url) {
    return new Promise((resolve, reject) => {
      axios
        .get(url)
        .then(res => {
          resolve(res.data);
        })
        .catch(error => {
          reject(error);
        });
    });
  },
  //post请求
  requestPost(url, params = {}) {
    return new Promise((resolve, reject) => {
      axios
        .post(url, params)
        .then(res => {
          resolve(res.data);
        })
        .catch(error => {
          reject(error);
        });
    });
  },
  //post请求
  requestPostForm(url, params = {}) {
    return new Promise((resolve, reject) => {
      axios
        .post(url, params, {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          }
        })
        .then(res => {
          resolve(res.data); //注意res是axios封装的对象，res.data才是服务端返回的信息
        })
        .catch(error => {
          reject(error);
        });
    });
  },
  //put请求
  requestPut(url, params = {}) {
    return new Promise((resolve, reject) => {
      axios
        .put(url, params)
        .then(res => {
          resolve(res.data);
        })
        .catch(error => {
          reject(error);
        });
    });
  },
  //delete请求
  requestDelete(url, params = {}) {
    return new Promise((resolve, reject) => {
      axios
        .delete(url, params)
        .then(res => {
          resolve(res.data);
        })
        .catch(error => {
          reject(error);
        });
    });
  }
};
