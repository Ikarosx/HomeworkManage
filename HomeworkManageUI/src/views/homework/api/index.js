import http from "@/api/public";
import { systemConfig } from "@/../config/system";

const apiUrl = systemConfig.apiUrl;

export const insertHomework = params => {
  return http.requestPost(apiUrl + "/manageHomework", params);
};

export const listHomeworks = () => {
  return http.requestQuickGet(apiUrl + "/manageHomework/current/list/all");
};

export const updateHomework = params => {
  return http.requestPut(apiUrl + "/manageHomework/" + params.id, params);
};

export const deleteHomework = id => {
  return http.requestDelete(apiUrl + "/manageHomework/" + id);
};

export const insertManageHomeworkUser = params => {
  return http.requestPost(apiUrl + "/manageHomeworkUser", params);
};
export const updateManageHomeworkUser = params => {
  return http.requestPut(apiUrl + "/manageHomeworkUser/" + params.id, params);
};

export const getHomeworkFinishInfo = params => {
  return http.requestGet(
    apiUrl +
      "/manageHomework/" +
      params.homeworkId +
      "/finishinfo?classId=" +
      params.classId
  );
};
export const downloadHomeworkFinishInfos = params => {
  window.open(
    apiUrl +
      "/manageHomework/" +
      params.id +
      "/finishinfo/download?classId=" +
      params.classId
  );
};

export const uploadFile = params => {
  return http.requestPostForm(apiUrl + "/file/upload", params);
};

export const getHomeworkListByHomeworkUserId = id => {
  return http.requestGet(apiUrl + "/manageHomeworkUser/" + id + "/files");
};
