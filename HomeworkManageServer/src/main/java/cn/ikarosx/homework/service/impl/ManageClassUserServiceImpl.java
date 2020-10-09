package cn.ikarosx.homework.service.impl;

import cn.ikarosx.homework.constant.ManageClassUserStatusEnum;
import cn.ikarosx.homework.entity.ManageClassUser;
import cn.ikarosx.homework.entity.User;
import cn.ikarosx.homework.exception.ClassCodeEnum;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.CustomException;
import cn.ikarosx.homework.exception.ExceptionCast;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.query.ManageClassUserQueryParam;
import cn.ikarosx.homework.model.param.update.ManageClassUserUpdateParam;
import cn.ikarosx.homework.repository.ManageClassRepository;
import cn.ikarosx.homework.repository.ManageClassUserRepository;
import cn.ikarosx.homework.repository.UserRepository;
import cn.ikarosx.homework.service.ManageClassService;
import cn.ikarosx.homework.service.ManageClassUserService;
import cn.ikarosx.homework.util.SessionUtils;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ikarosx
 * @date 2020/08/16 14:24
 */
@Service
public class ManageClassUserServiceImpl implements ManageClassUserService {

  @Autowired private ManageClassUserRepository manageClassUserRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private ManageClassService manageClassService;
  @Autowired private ManageClassRepository manageClassRepository;

  @Override
  public String insertManageClassUser(ManageClassUser manageClassUser) {
    manageClassUserRepository.save(manageClassUser);
    return manageClassUser.getClassId();
  }

  @Override
  public ResponseResult deleteManageClassUserByUserId(String id) {
    manageClassUserRepository.deleteAllByUserId(id);
    return CommonCodeEnum.SUCCESS;
  }

  @Override
  public ResponseResult updateManageClassUser(
      ManageClassUserUpdateParam manageClassUserUpdateParam) {
    ManageClassUser manageClassUser = new ManageClassUser();
    BeanUtils.copyProperties(manageClassUserUpdateParam, manageClassUser);
    manageClassUserRepository.save(manageClassUser);
    return CommonCodeEnum.SUCCESS;
  }

  @Override
  public ManageClassUser getManageClassUserById(String id) {
    ManageClassUser manageClassUser =
        manageClassUserRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    return manageClassUser;
  }

  @Override
  public ResponseResult listManageClassUsersByPage(
      int page, int size, ManageClassUserQueryParam manageClassUserQueryParam) {
    ManageClassUser manageClassUser = new ManageClassUser();
    BeanUtils.copyProperties(manageClassUserQueryParam, manageClassUser);
    // 筛选
    ExampleMatcher matcher =
        ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example<ManageClassUser> example = Example.of(manageClassUser, matcher);
    // 分页
    Pageable pageable = PageRequest.of(page - 1, size);
    Page<ManageClassUser> manageClassUserPage =
        manageClassUserRepository.findAll(example, pageable);
    return CommonCodeEnum.SUCCESS.addData(
        "list",
        manageClassUserPage.getContent(),
        "total",
        manageClassUserPage.getTotalElements(),
        "totalPage",
        manageClassUserPage.getTotalPages());
  }

  @Override
  public ResponseResult listAllManageClassUsers() {
    List<ManageClassUser> list = manageClassUserRepository.findAll();
    return CommonCodeEnum.SUCCESS.addData("list", list, "total", list.size());
  }

  @Override
  public void deleteManageClassUserByClassId(String id) {
    manageClassUserRepository.deleteAllByClassId(id);
  }

  /**
   * 根据传入的对象查询数据
   *
   * @param manageClassUser
   * @return
   */
  @Override
  public ManageClassUser getByManageClassUser(ManageClassUser manageClassUser) {
    return manageClassUserRepository.findOne(Example.of(manageClassUser)).orElse(null);
  }

  /** 管理员处理申请 */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void dealApply(ManageClassUserUpdateParam manageClassUserUpdateParam) {
    ManageClassUser manageClassUser =
        manageClassUserRepository
            .findByUserIdAndClassId(
                manageClassUserUpdateParam.getUserId(), manageClassUserUpdateParam.getClassId())
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    String classId = manageClassUser.getClassId();
    // 如果不是属于这个班级的直接无权限
    if (SessionUtils.getClassId() == null
        || !StringUtils.equals(classId, SessionUtils.getClassId())) {
      ExceptionCast.cast(CommonCodeEnum.PERMISSION_DENY);
    }
    // 只有班级管理员可以处理
    if (!StringUtils.equals(
        manageClassService.getManageClassById(manageClassUser.getClassId()).getAdminUserId(),
        SessionUtils.getId())) {
      ExceptionCast.cast(CommonCodeEnum.PERMISSION_DENY);
    }
    String userId = manageClassUser.getUserId();
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    // 如果该用户已经加入了其他班级
    if (StringUtils.isNotBlank(user.getClassId())) {
      // 删除申请记录
      manageClassUserRepository.delete(manageClassUser);
      ExceptionCast.cast(ClassCodeEnum.AGREE_ERROR);
    }
    // 拒绝把状态设置为拒绝
    /*if (manageClassUserUpdateParam
        .getStatus()
        .equals(ManageClassUserStatusEnum.Rejected.getStatus())) {
            manageClassUserRepository.delete(manageClassUser);
            return;
    }*/
    manageClassUser.setStatus(manageClassUserUpdateParam.getStatus());
    manageClassUserRepository.save(manageClassUser);
    // 如果是同意申请的话，需要更改用户
    if (manageClassUserUpdateParam
        .getStatus()
        .equals(ManageClassUserStatusEnum.Joined.getStatus())) {
      user.setClassId(manageClassUser.getClassId());
      userRepository.save(user);
    }
  }
}
