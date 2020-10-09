package cn.ikarosx.homework.repository;

import cn.ikarosx.homework.entity.ManageHomeworkFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @author Ikarosx
 * @date 2020/09/16 21:40
 */
public interface ManageHomeworkFileRepository extends JpaRepository<ManageHomeworkFile, String> {

    List<ManageHomeworkFile> findByHomeworkUserId(String id);
}
