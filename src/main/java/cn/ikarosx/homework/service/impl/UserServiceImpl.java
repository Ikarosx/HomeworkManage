package cn.ikarosx.homework.service.impl;

import cn.ikarosx.homework.entity.ManageClass;
import cn.ikarosx.homework.entity.User;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.CustomException;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.query.UserQueryParam;
import cn.ikarosx.homework.model.param.update.UserUpdateParam;
import cn.ikarosx.homework.repository.ManageClassRepository;
import cn.ikarosx.homework.repository.UserRepository;
import cn.ikarosx.homework.service.UserService;
import cn.ikarosx.homework.util.SessionUtils;
import cn.ikarosx.homework.util.UpdateUtils;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserRepository userRepository;
  @Autowired private ManageClassRepository manageClassRepository;

  @Override
  public String insertUser(User user) {
    //    User existUser = userRepository.findByUsername(user.getUsername());
    //    // 如果用户名已经存在
    //    if (existUser != null) {
    //      ExceptionCast.cast(ClassCodeEnum.USER_NAME_EXIST_ERROR);
    //    }
    user.setType(0);
    userRepository.save(user);
    return user.getId();
  }

  @Override
  public ResponseResult deleteUserById(String id) {
    userRepository.deleteById(id);
    return CommonCodeEnum.SUCCESS.clearData();
  }

  @Override
  public ResponseResult updateUser(UserUpdateParam userUpdateParam) {
    User user =
        userRepository
            .findById(userUpdateParam.getId())
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    UpdateUtils.copyNullProperties(userUpdateParam, user);
    userRepository.save(user);
    return CommonCodeEnum.SUCCESS.clearData();
  }

  @Override
  public ResponseResult getUserById(String id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    SessionUtils.setAttribute("user", user);
    return CommonCodeEnum.SUCCESS.addData("user", user);
  }

  @Override
  public ResponseResult listUsersByPage(int page, int size, UserQueryParam userQueryParam) {
    User user = new User();
    BeanUtils.copyProperties(userQueryParam, user);
    // 筛选
    ExampleMatcher matcher =
        ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example<User> example = Example.of(user, matcher);
    // 分页
    Pageable pageable = PageRequest.of(page - 1, size);
    Page<User> userPage = userRepository.findAll(example, pageable);
    return CommonCodeEnum.SUCCESS.addData(
        "list",
        userPage.getContent(),
        "total",
        userPage.getTotalElements(),
        "totalPage",
        userPage.getTotalPages());
  }

  @Override
  public ResponseResult listAllUsers() {
    List<User> list = userRepository.findAll();
    return CommonCodeEnum.SUCCESS.addData("list", list, "total", list.size());
  }

  @Override
  public ResponseResult login(String username, String password) {
    User user = new User();
    user.setUsername(username);
    Example<User> example = Example.of(user);
    Optional<User> optional = userRepository.findOne(example);
    user =
        optional
            .map(u -> BCrypt.checkpw(password, u.getPassword()) ? u : null)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.USERNAME_OR_PASSWORD_ERROR));
    SessionUtils.setAttribute("user", user);
    return CommonCodeEnum.SUCCESS.addData("user", user);
  }

  @Override
  public ResponseResult logout() {
    SessionUtils.invalidate();
    return CommonCodeEnum.SUCCESS.clearData();
  }

  @Override
  public boolean isClassMember() {
    return StringUtils.isNotBlank(SessionUtils.getClassId());
  }

  /**
   * 判断当前用户是否是传入班级ID的管理员
   *
   * @param classId
   * @return
   */
  @Override
  public boolean isClassOwner(String classId) {
    return StringUtils.equals(
        manageClassRepository.findById(classId).orElse(new ManageClass()).getAdminUserId(),
        SessionUtils.getId());
  }

  @Override
  public boolean isAdmin() {
    return SessionUtils.isAdmin();
  }

  /**
   * 退出班级后清除ClassId
   *
   * @param id
   */
  @Override
  public void clearClassIdByUserId(String id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    user.setClassId(null);
    userRepository.save(user);
  }

  @Override
  public void updateClassIdByUserId(String id, String classId) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    user.setClassId(classId);
    userRepository.save(user);
  }

  @Override
  public User getUserByUserName(String username) {
    return userRepository.findByUsername(username);
  }
}
