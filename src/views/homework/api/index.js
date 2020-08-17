import http from "@/api/public";
import { systemConfig } from "@/../config/system";

const apiUrl = systemConfig.apiUrl;

export const listAllManageClass = () => {
  return http.requestGet(apiUrl + "/manageClass/list/all");
};

export const insertManageClass = param => {
  return http.requestPost(apiUrl + "/manageClass", param);
};

export const getManageClassDetailInfo = id => {
  return http.requestGet(apiUrl + "/manageClass/" + id + "/detail");
};

export const deleteManageClassUser = id => {
  return http.requestDelete(apiUrl + "/manageClassUser/" + id);
};

export const insertManageClassUser = params => {
  return http.requestPost(apiUrl + "/manageClassUser", params);
};

export const deleteClass = id => {
  return http.requestDelete(apiUrl + "/manageClass/" + id);
}