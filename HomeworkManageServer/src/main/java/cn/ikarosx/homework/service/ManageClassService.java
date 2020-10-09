package cn.ikarosx.homework.service;

import cn.ikarosx.homework.entity.ManageClass;
import cn.ikarosx.homework.entity.User;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.BO.ManageClassDetailInfoUser;
import cn.ikarosx.homework.model.param.insert.ManageClassInsertParam;
import cn.ikarosx.homework.model.param.query.ManageClassQueryParam;
import cn.ikarosx.homework.model.param.update.ManageClassUpdateParam;
import java.util.List;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
public interface ManageClassService {
  String insertManageClass(ManageClassInsertParam manageClassInsertParam);

  ResponseResult deleteManageClassById(String id);

  ResponseResult updateManageClass(ManageClassUpdateParam manageClassUpdateParam);

  ManageClass getManageClassById(String id);

  ResponseResult listManageClasssByPage(
      int page, int size, ManageClassQueryParam manageClassQueryParam);

  ResponseResult listAllManageClasss();

  List<ManageClassDetailInfoUser> listAllManageClassUser(String classId, Integer status);
}
