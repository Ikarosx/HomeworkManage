package cn.ikarosx.homework.service;

import cn.ikarosx.homework.entity.ManageHomework;
import cn.ikarosx.homework.model.BO.HomeworkFinishInfo;
import cn.ikarosx.homework.model.BO.ManageHomeworkDetails;
import cn.ikarosx.homework.model.param.query.ManageHomeworkQueryParam;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * @author Ikarosx
 * @date 2020/08/17 14:16
 */
public interface ManageHomeworkService {
  void insertManageHomework(ManageHomework manageHomework);

  void deleteManageHomeworkById(String id);

  void updateManageHomework(ManageHomework manageHomework);

  ManageHomework getManageHomeworkById(String id);

  Page<ManageHomework> listManageHomeworksByPage(
      int page, int size, ManageHomeworkQueryParam manageHomeworkQueryParam);

  List<ManageHomework> listAllManageHomeworks();

  boolean hasPermissionOnHomeworkByHomeworkId(String homeworkId);

  List<ManageHomeworkDetails> listAllManageHomeworksByCurrentUser();

  List<HomeworkFinishInfo> getHomeworkFinishInfo(String classId, String homeworkId);

  void downloadHomeworkAllFiles(ManageHomework manageHomework);
}
