package cn.ikarosx.homework.controller;

import cn.ikarosx.homework.entity.ManageClass;
import cn.ikarosx.homework.entity.ManageClassUser;
import cn.ikarosx.homework.entity.User;
import cn.ikarosx.homework.exception.ClassCodeEnum;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.CustomException;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.BO.ManageClassDetailInfo;
import cn.ikarosx.homework.model.BO.ManageClassDetailInfoUser;
import cn.ikarosx.homework.model.param.insert.ManageClassInsertParam;
import cn.ikarosx.homework.model.param.query.ManageClassQueryParam;
import cn.ikarosx.homework.model.param.update.ManageClassUpdateParam;
import cn.ikarosx.homework.service.ManageClassService;
import cn.ikarosx.homework.service.ManageClassUserService;
import cn.ikarosx.homework.service.UserService;
import cn.ikarosx.homework.util.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
@RestController
@RequestMapping("manageClass")
@Api(tags = "班级")
public class ManageClassController {
  @Autowired private ManageClassService manageClassService;
  @Autowired private ManageClassUserService manageClassUserService;
  @Autowired private UserService userService;

  @PostMapping
  @ApiOperation(value = "新增班级,创建人初始化为管理员")
  @Transactional
  public ResponseResult insertManageClass(
      @Validated @RequestBody ManageClassInsertParam manageClassInsertParam) {
    // 判断当前用户是否已经加入了班级
    if (StringUtils.isNotBlank(SessionUtils.getClassId())) {
      return ClassCodeEnum.CLASS_MEMBER_CAN_NOT_CREATE_CLASS;
    }
    // 新增班级，返回班级ID
    String manageClassId = manageClassService.insertManageClass(manageClassInsertParam);
    // 更新用户
    User user = SessionUtils.getUser();
    user.setClassId(manageClassId);
    // 更新数据库用户所属班级
    userService.insertUser(user);
    SessionUtils.setAttribute("user", user);
    ManageClassUser manageClassUser = new ManageClassUser();
    // 该用户应该为普通用户
    manageClassUser.setStatus(0);
    manageClassUser.setUserId(SessionUtils.getId());
    manageClassUser.setClassId(manageClassId);
    manageClassUserService.insertManageClassUser(manageClassUser);
    return CommonCodeEnum.SUCCESS.addData("classId", manageClassId);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "通过ID删除班级,同时清除班级成员")
  @Transactional
  public ResponseResult deleteManageClassById(@PathVariable String id) {
    ManageClass manageClass = manageClassService.getManageClassById(id);
    // 班级的管理员与当前用户的ID是否相同
    if (!StringUtils.equals(manageClass.getAdminUserId(), SessionUtils.getId())) {
      return CommonCodeEnum.PERMISSION_DENY;
    }
    // 删除班级本身
    manageClassService.deleteManageClassById(id);
    // 删除班级成员
    manageClassUserService.deleteManageClassUserByClassId(id);
    // 清除当前用户所属班级
    userService.clearClassIdByUserId(SessionUtils.getId());
    // 更新Session
    User user = SessionUtils.getUser();
    user.setClassId(null);
    SessionUtils.setAttribute("user", user);
    return CommonCodeEnum.SUCCESS;
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "更新班级")
  public ResponseResult updateManageClass(
      @PathVariable String id,
      @Validated @RequestBody ManageClassUpdateParam manageClassUpdateParam) {
    return manageClassService.updateManageClass(manageClassUpdateParam);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "通过ID查询班级")
  public ResponseResult getManageClassById(@PathVariable String id) {
    ManageClass manageClass = manageClassService.getManageClassById(id);
    return CommonCodeEnum.SUCCESS.addData("manageClass", manageClass);
  }

  @GetMapping("/list/{page}/{size}")
  @ApiOperation(value = "分页查询班级")
  public ResponseResult listManageClasssByPage(
      @PathVariable int page, @PathVariable int size, ManageClassQueryParam manageClassQueryParam) {
    if (page < 1) {
      page = 1;
    }
    if (size <= 0) {
      size = 10;
    }
    if (manageClassQueryParam == null) {
      manageClassQueryParam = new ManageClassQueryParam();
    }
    return manageClassService.listManageClasssByPage(page, size, manageClassQueryParam);
  }

  @GetMapping("/list/all")
  @ApiOperation(value = "查询全部班级")
  public ResponseResult listAllManageClasses() {
    return manageClassService.listAllManageClasss();
  }

  @GetMapping("/{classId}/user/all")
  @ApiOperation(value = "查询班级成员")
  public ResponseResult listAllManageClassUser(@PathVariable String classId, Integer status) {
    List<ManageClassDetailInfoUser> list =
        manageClassService.listAllManageClassUser(classId, status);
    return CommonCodeEnum.SUCCESS.addData("list", list);
  }

  @GetMapping("/{id}/detail")
  @ApiOperation(value = "获取班级详细信息")
  public ResponseResult getManageClassDetailInfo(@PathVariable String id) {
    if (!SessionUtils.isAdmin() && !StringUtils.equals(SessionUtils.getClassId(), id)) {
      // 不是班级成员无法查询详细信息
      return CommonCodeEnum.PERMISSION_DENY;
    }
    ManageClass manageClass = manageClassService.getManageClassById(id);
    ManageClassDetailInfo manageClassDetailInfo = new ManageClassDetailInfo();
    BeanUtils.copyProperties(manageClass, manageClassDetailInfo);
    manageClassDetailInfo.setMembers(manageClassService.listAllManageClassUser(id, 0));
    manageClassDetailInfo.setNum(manageClassDetailInfo.getMembers().size());
    // 从成员中过滤出管理员
    manageClassDetailInfo.setAdminUser(
        manageClassDetailInfo.getMembers().stream()
            .filter(user -> StringUtils.equals(user.getId(), manageClass.getAdminUserId()))
            .findFirst()
            .orElseThrow(() -> new CustomException(ClassCodeEnum.ADMIN_NOT_FOUND_ERROR)));
    return CommonCodeEnum.SUCCESS

        .addData("manageClassDetailInfo", manageClassDetailInfo);
  }
}
