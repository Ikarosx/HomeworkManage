package cn.ikarosx.homework.service;

import cn.ikarosx.homework.entity.ManageHomeworkFile;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.insert.ManageHomeworkFileInsertParam;
import cn.ikarosx.homework.model.param.query.ManageHomeworkFileQueryParam;
import cn.ikarosx.homework.model.param.update.ManageHomeworkFileUpdateParam;
import java.util.List;

/**
 * @author Ikarosx
 * @date 2020/09/16 21:40
 */
public interface ManageHomeworkFileService {
  ResponseResult insertManageHomeworkFile(
      ManageHomeworkFileInsertParam manageHomeworkFileInsertParam);

  ResponseResult deleteManageHomeworkFileById(String id);

  ResponseResult updateManageHomeworkFile(
      ManageHomeworkFileUpdateParam manageHomeworkFileUpdateParam);

  ResponseResult getManageHomeworkFileById(String id);

  ResponseResult listManageHomeworkFilesByPage(
      int page, int size, ManageHomeworkFileQueryParam manageHomeworkFileQueryParam);

  ResponseResult listAllManageHomeworkFiles();

  List<ManageHomeworkFile> getManageHomeworkFileListByHomeWorkUserId(String id);
}
