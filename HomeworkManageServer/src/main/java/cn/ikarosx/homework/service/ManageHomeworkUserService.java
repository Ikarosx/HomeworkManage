package cn.ikarosx.homework.service;

import cn.ikarosx.homework.entity.ManageHomeworkUser;
import cn.ikarosx.homework.model.param.insert.ManageHomeworkUserInsertParam;
import cn.ikarosx.homework.model.param.query.ManageHomeworkUserQueryParam;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * @author Ikarosx
 * @date 2020/08/17 20:29
 */
public interface ManageHomeworkUserService {
  void insertManageHomeworkUser(ManageHomeworkUser manageHomeworkUser);

  void deleteManageHomeworkUserById(String id);

  void updateManageHomeworkUser(ManageHomeworkUser manageHomeworkUser);

  ManageHomeworkUser getManageHomeworkUserById(String id);

  Page<ManageHomeworkUser> listManageHomeworkUsersByPage(
      int page, int size, ManageHomeworkUserQueryParam manageHomeworkUserQueryParam);

  List<ManageHomeworkUser> listAllManageHomeworkUsers();

  String submitHomework(ManageHomeworkUserInsertParam manageHomeworkUserInsertParam);

  void downloadManageHomeworkUserById(String id,String fileIds);
}
