package cn.ikarosx.homework.repository;

import cn.ikarosx.homework.entity.ManageClassUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @author Ikarosx
 * @date 2020/08/16 14:24
 */
public interface ManageClassUserRepository extends JpaRepository<ManageClassUser, String> {

  void deleteAllByUserId(String userId);

  void deleteAllByClassId(String classId);

  Optional<ManageClassUser> findByUserIdAndClassId(String userId, String classId);
}
