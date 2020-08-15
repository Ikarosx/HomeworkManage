package cn.ikarosx.homework.controller;

import cn.ikarosx.homework.exception.ClassCodeEnum;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.insert.ManageClassInsertParam;
import cn.ikarosx.homework.model.param.query.ManageClassQueryParam;
import cn.ikarosx.homework.model.param.update.ManageClassUpdateParam;
import cn.ikarosx.homework.service.ManageClassService;
import cn.ikarosx.homework.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired private UserService userService;

  @PostMapping
  @ApiOperation(value = "新增班级")
  public ResponseResult insertManageClass(
      @Validated @RequestBody ManageClassInsertParam manageClassInsertParam) {
    if (userService.isClassMember()) {
      return ClassCodeEnum.CLASS_MEMBER_CAN_NOT_CREATE_CLASS;
    }
    return manageClassService.insertManageClass(manageClassInsertParam);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "通过ID删除班级")
  public ResponseResult deleteManageClassById(@PathVariable String id) {
    return manageClassService.deleteManageClassById(id);
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
    return manageClassService.getManageClassById(id);
  }

  @GetMapping("/{page}/{size}")
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

  @GetMapping
  @ApiOperation(value = "查询全部班级")
  public ResponseResult listAllManageClasss() {
    return manageClassService.listAllManageClasss();
  }
}
