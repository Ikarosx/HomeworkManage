package cn.ikarosx.homework.repository;

import cn.ikarosx.homework.entity.ManageHomeworkFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Ikarosx
 * @date 2020/09/16 21:40
 */
public interface ManageHomeworkFileRepository extends JpaRepository<ManageHomeworkFile, String> {

    List<ManageHomeworkFile> findByHomeworkUserId(String id);

    List<ManageHomeworkFile> findByHomeworkUserIdAndFileIdIn(@Param("homeworkUserId") String homeworkUserId, @Param("fileId") List<String> fileId);

    List<ManageHomeworkFile> findByHomeworkUserIdAndIdIn(@Param("homeworkUserId")String id,@Param("id") List<String> idsByUser);
}
