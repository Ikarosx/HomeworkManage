import http from "@/api/public";
import querystring from "querystring";
import { systemConfig } from "@/../config/system";
let apiUrl = systemConfig.bdApiUrl;
export const listGoods = (page, size, params) => {
  let query = querystring.stringify(params);
  return http.requestGet(
    apiUrl + "/good/list/" + page + "/" + size + "?" + query
  );
};

export const insertGood = params => {
  return http.requestPost(apiUrl + "/good", params);
};

export const deleteGood = id => {
  return http.requestDelete(apiUrl + "/good/" + id);
};

export const listCategorys = params => {
  let query = querystring.stringify(params);
  return http.requestGet(apiUrl + "/category/list/?" + query);
};

export const listCategoryStruct = () => {
  return http.requestGet(apiUrl + "/category/struct");
};

export const insertCategory = params => {
    return http.requestPost(apiUrl + "/category/", params);
}

export const deleteCategory = id => {
    return http.requestDelete(apiUrl + "/category/" + id);
}

