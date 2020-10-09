import http from '@/api/public'
import querystring from 'querystring'
import { systemConfig } from "@/../config/system"
let apiUrlPre = systemConfig.bdApiUrl;
let apiUrl = systemConfig.bdApiUrl;
export const getUserList = (page, size, params) => {
  let query = querystring.stringify(params);
  return http.requestGet(apiUrl + '/ucenter/user/list/' + page + '/' + size + '?' + query);
}

export const insertUser = (params) => {
  return http.requestPost(apiUrl + '/ucenter/user', params);
}

export const deleteUser = (id) => {
  return http.requestDelete(apiUrl + '/ucenter/user/' + id);
}

export const updateUser = (params, id) => {
  return http.requestPut(apiUrl + '/ucenter/user/' + id, params);
}

