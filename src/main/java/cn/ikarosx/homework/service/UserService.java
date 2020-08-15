package cn.ikarosx.homework.service;

import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.insert.UserInsertParam;
import cn.ikarosx.homework.model.param.query.UserQueryParam;
import cn.ikarosx.homework.model.param.update.UserUpdateParam;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
public interface UserService {
  ResponseResult insertUser(UserInsertParam userInsertParam);

  ResponseResult deleteUserById(String id);

  ResponseResult updateUser(UserUpdateParam userUpdateParam);

  ResponseResult getUserById(String id);

  ResponseResult listUsersByPage(int page, int size, UserQueryParam userQueryParam);

  ResponseResult listAllUsers();

  ResponseResult login(String username, String password);

  ResponseResult logout();

  boolean isClassMember();

  boolean isClassOwner(String classId);

  boolean isAdmin();
}
