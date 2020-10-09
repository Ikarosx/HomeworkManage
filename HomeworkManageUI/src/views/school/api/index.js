import http from "@/api/public"
import querystring from "querystring"
import { systemConfig } from "@/../config/system"
let apiUrl = systemConfig.bdApiUrl;
export const listSchools = (params) => {
    let query = querystring.stringify(params);
    return http.requestGet(apiUrl + '/ucenter/school/list/' + '?' + query);
}

export const insertSchool = params => {
    return http.requestPost(apiUrl + '/ucenter/school', params);
}

export const deleteSchool = id => {
    return http.requestDelete(apiUrl + '/ucenter/school/' + id);
}

export const updateSchool = (school, id) => {
    return http.requestPut(apiUrl + '/ucenter/school/' + id, school);
}

export const listSchoolSystems = () => {
    return http.requestQuickGet(apiUrl + '/ucenter/school/system/list/');
}

export const insertSchoolSystem = (params) => {
    return http.requestPost(apiUrl + '/ucenter/school/system/', params);
}

export const deleteSchoolSystem = (id) => {
    return http.requestDelete(apiUrl + '/ucenter/school/system/' + id);
}

export const updateSchoolSystem = (system, id) => {
    return http.requestPut(apiUrl + '/ucenter/school/system/' + id, system);
}
