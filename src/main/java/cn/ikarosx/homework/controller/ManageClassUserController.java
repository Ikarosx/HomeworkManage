package cn.ikarosx.homework.controller;

import cn.ikarosx.homework.constant.ManageClassUserStatusEnum;
import cn.ikarosx.homework.entity.ManageClass;
import cn.ikarosx.homework.entity.ManageClassUser;
import cn.ikarosx.homework.entity.User;
import cn.ikarosx.homework.exception.ClassCodeEnum;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.insert.ManageClassUserInsertParam;
import cn.ikarosx.homework.model.param.query.ManageClassUserQueryParam;
import cn.ikarosx.homework.model.param.update.ManageClassUserUpdateParam;
import cn.ikarosx.homework.service.ManageClassService;
import cn.ikarosx.homework.service.ManageClassUserService;
import cn.ikarosx.homework.service.UserService;
import cn.ikarosx.homework.util.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Ikarosx
 * @date 2020/08/16 14:24
 */
@RestController
@RequestMapping("manageClassUser")
@Api(tags = "班级-成员")
public class ManageClassUserController {
  @Autowired private ManageClassUserService manageClassUserService;
  @Autowired private ManageClassService manageClassService;
  @Autowired private UserService userService;

  @PostMapping
  @ApiOperation(value = "用户申请加入班级", notes = "自己申请加入班级，需要班级管理员同意才可以通过")
  @Transactional
  public ResponseResult insertManageClassUser(
      @Validated @RequestBody ManageClassUserInsertParam manageClassUserInsertParam) {
    if (StringUtils.isNotBlank(SessionUtils.getClassId())) {
      return ClassCodeEnum.CLASS_MEMBER_CAN_NOT_JSON_CLASS;
    }
    ManageClass manageClass =
        manageClassService.getManageClassById(manageClassUserInsertParam.getClassId());
    // 班级不存在
    if (manageClass == null) {
      return ClassCodeEnum.CLASS_NOT_FOUNT_ERROR;
    }
    ManageClassUser manageClassUser = new ManageClassUser();
    manageClassUser.setUserId(SessionUtils.getId());
    // 申请加入，状态为等待
    manageClassUser.setStatus(ManageClassUserStatusEnum.Waiting.getStatus());
    // 判断是否已经申请过了
    boolean flag = manageClassUserService.getByManageClassUser(manageClassUser);
    if (flag) {
      return ClassCodeEnum.WAITING_ADMIN_AGREE;
    }
    manageClassUser.setClassId(manageClassUserInsertParam.getClassId());
    manageClassUserService.insertManageClassUser(manageClassUser);
    return CommonCodeEnum.SUCCESS.clearData();
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "用户退出班级")
  @Transactional
  public ResponseResult deleteManageClassUserByUserId(@PathVariable String id) {
    // 班级管理员可以踢成员 TODO
    // 被拒绝也直接删除 TODO
    // 自己可以退出
    // 管理员可以操作任何人
    if (!StringUtils.equals(id, SessionUtils.getId()) && !SessionUtils.isAdmin()) {
      return CommonCodeEnum.PERMISSION_DENY;
    }
    manageClassUserService.deleteManageClassUserByUserId(id);
    userService.clearClassIdByUserId(id);
    User user = SessionUtils.getUser();
    user.setClassId(null);
    SessionUtils.setAttribute("user", user);
    return CommonCodeEnum.SUCCESS.clearData();
  }

  @PostMapping("/deal")
  @ApiOperation(value = "处理申请", notes = "status为012分别表示已经通过，等待中，拒绝")
  public ResponseResult dealApply(
      @Validated @RequestBody ManageClassUserUpdateParam manageClassUserUpdateParam) {
    manageClassUserService.dealApply(manageClassUserUpdateParam);
    return CommonCodeEnum.SUCCESS.clearData();
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "通过ID查询班级-成员")
  public ResponseResult getManageClassUserById(@PathVariable String id) {
    return manageClassUserService.getManageClassUserById(id);
  }

  @GetMapping("/list/{page}/{size}")
  @ApiOperation(value = "分页查询班级-成员")
  public ResponseResult listManageClassUsersByPage(
      @PathVariable int page,
      @PathVariable int size,
      ManageClassUserQueryParam manageClassUserQueryParam) {
    if (page < 1) {
      page = 1;
    }
    if (size <= 0) {
      size = 10;
    }
    if (manageClassUserQueryParam == null) {
      manageClassUserQueryParam = new ManageClassUserQueryParam();
    }
    return manageClassUserService.listManageClassUsersByPage(page, size, manageClassUserQueryParam);
  }

  @GetMapping("/list/all")
  @ApiOperation(value = "查询全部班级-成员")
  public ResponseResult listAllManageClassUsers() {
    return manageClassUserService.listAllManageClassUsers();
  }
}
