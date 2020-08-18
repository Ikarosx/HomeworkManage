package cn.ikarosx.homework.repository;

import cn.ikarosx.homework.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
public interface UserRepository extends JpaRepository<User, String> {}
