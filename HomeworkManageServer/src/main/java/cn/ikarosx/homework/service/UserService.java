package cn.ikarosx.homework.service;

import cn.ikarosx.homework.entity.User;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.query.UserQueryParam;
import cn.ikarosx.homework.model.param.update.UserUpdateParam;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
public interface UserService {
  String insertUser(User user);

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

  void clearClassIdByUserId(String id);

  void updateClassIdByUserId(String id, String classId);

  User getUserByUserName(String username);
}
