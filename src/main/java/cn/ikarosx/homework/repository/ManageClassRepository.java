package cn.ikarosx.homework.repository;

import cn.ikarosx.homework.entity.ManageClass;
import cn.ikarosx.homework.model.BO.ManageClassDetailInfoUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
public interface ManageClassRepository extends JpaRepository<ManageClass, String> {
  @Query(
      value =
          "SELECT new cn.ikarosx.homework.model.BO.ManageClassDetailInfoUser(u.id, u.username, u.nickname, u.studentNo, cu.createTime) FROM User u LEFT JOIN ManageClassUser cu ON u.id = cu.userId WHERE cu.classId = :classId AND (:status IS NULL OR :status = cu.status)")
  List<ManageClassDetailInfoUser> findAllByClassIdAndStatus(String classId, Integer status);
}
