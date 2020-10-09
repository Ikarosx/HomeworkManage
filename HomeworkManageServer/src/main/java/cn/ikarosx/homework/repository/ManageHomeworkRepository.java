package cn.ikarosx.homework.repository;

import cn.ikarosx.homework.entity.ManageHomework;
import cn.ikarosx.homework.model.BO.HomeworkFinishInfo;
import cn.ikarosx.homework.model.BO.ManageHomeworkDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Ikarosx
 * @date 2020/08/17 14:16
 */
public interface ManageHomeworkRepository extends JpaRepository<ManageHomework, String> {

  @Query(
      "SELECT c.adminUserId FROM ManageClass c LEFT JOIN ManageHomework h ON h.classId = c.id WHERE h.id = :homeworkId")
  String getAdminUserIdByHomeworkId(@Param("homeworkId") String homeworkId);

  @Query(
      "SELECT new cn.ikarosx.homework.model.BO.ManageHomeworkDetails(h.id, h.title, h.description, h.deadline, CASE hu.status WHEN 1 then true ELSE false END, h.createTime, h.updateTime) FROM ManageHomework h LEFT JOIN ManageHomeworkUser hu ON hu.homeworkId = h.id AND hu.userId = :id WHERE h.classId = :classId")
  List<ManageHomeworkDetails> listAllManageHomeworksByCurrentUser(
      @Param("id") String id, @Param("classId") String classId);

  @Query(
      "SELECT new cn.ikarosx.homework.model.BO.HomeworkFinishInfo(hu.id, u.nickname, u.studentNo, hu.status, hu.createTime) FROM User u LEFT JOIN  ManageHomeworkUser hu ON hu.userId = u.id AND hu.homeworkId = :homeworkId WHERE u.classId = :classId ORDER BY hu.status, u.studentNo")
  List<HomeworkFinishInfo> getHomeworkFinishInfo(
      @Param("classId") String classId, @Param("homeworkId") String homeworkId);
}
