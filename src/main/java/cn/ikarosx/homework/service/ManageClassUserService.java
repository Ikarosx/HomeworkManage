package cn.ikarosx.homework.service;

import cn.ikarosx.homework.entity.ManageClassUser;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.query.ManageClassUserQueryParam;
import cn.ikarosx.homework.model.param.update.ManageClassUserUpdateParam;

/**
 * @author Ikarosx
 * @date 2020/08/16 14:24
 */
public interface ManageClassUserService {
  String insertManageClassUser(ManageClassUser manageClassUser);

  ResponseResult deleteManageClassUserByUserId(String id);

  ResponseResult updateManageClassUser(ManageClassUserUpdateParam manageClassUserUpdateParam);

  ResponseResult getManageClassUserById(String id);

  ResponseResult listManageClassUsersByPage(
      int page, int size, ManageClassUserQueryParam manageClassUserQueryParam);

  ResponseResult listAllManageClassUsers();

  void deleteManageClassUserByClassId(String id);

  boolean getByManageClassUser(ManageClassUser manageClassUser);

  void dealApply(ManageClassUserUpdateParam manageClassUserUpdateParam);
}
