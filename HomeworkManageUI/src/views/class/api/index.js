import http from "@/api/public";
import { systemConfig } from "@/../config/system";

const apiUrl = systemConfig.apiUrl;

export const listWaitingUser = classId => {
  return http.requestGet(
    apiUrl + "/manageClass/" + classId + "/user/all?status=1"
  );
};

export const updateClassUser = params => {
  return http.requestPost(apiUrl + "/manageClassUser/deal", params);
};
