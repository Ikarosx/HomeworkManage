package cn.ikarosx.homework.controller;

import cn.ikarosx.homework.entity.ManageHomework;
import cn.ikarosx.homework.entity.ManageHomeworkUser;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.insert.ManageHomeworkUserInsertParam;
import cn.ikarosx.homework.model.param.query.ManageHomeworkUserQueryParam;
import cn.ikarosx.homework.model.param.update.ManageHomeworkUserUpdateParam;
import cn.ikarosx.homework.service.ManageHomeworkService;
import cn.ikarosx.homework.service.ManageHomeworkUserService;
import cn.ikarosx.homework.util.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Ikarosx
 * @date 2020/08/17 20:29
 */
@RestController
@RequestMapping("manageHomeworkUser")
@Api(tags = "作业提交记录")
public class ManageHomeworkUserController {
  @Autowired private ManageHomeworkUserService manageHomeworkUserService;
  @Autowired private ManageHomeworkService manageHomeworkService;

  @PostMapping
  @ApiOperation(value = "提交作业")
  public ResponseResult submitHomeworkUser(
      @Validated @RequestBody ManageHomeworkUserInsertParam manageHomeworkUserInsertParam) {
    String manageHomeWorkId =
        manageHomeworkUserService.submitHomework(manageHomeworkUserInsertParam);
    return CommonCodeEnum.SUCCESS.addData("manageHomeWorkId", manageHomeWorkId);
  }

  //  @DeleteMapping("/{id}")
  @ApiOperation(value = "通过ID删除作业提交记录")
  public ResponseResult deleteManageHomeworkUserById(@PathVariable String id) {
    manageHomeworkUserService.deleteManageHomeworkUserById(id);
    return CommonCodeEnum.SUCCESS;
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "更新作业提交记录")
  public ResponseResult updateManageHomeworkUser(
      @PathVariable String id,
      @Validated @RequestBody ManageHomeworkUserUpdateParam manageHomeworkUserUpdateParam) {
    ManageHomeworkUser manageHomeworkUser = new ManageHomeworkUser();
    BeanUtils.copyProperties(manageHomeworkUserUpdateParam, manageHomeworkUser);
    manageHomeworkUserService.updateManageHomeworkUser(manageHomeworkUser);
    return CommonCodeEnum.SUCCESS;
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "通过ID查询作业提交记录")
  public ResponseResult getManageHomeworkUserById(@PathVariable String id) {
    ManageHomeworkUser manageHomeworkUser = manageHomeworkUserService.getManageHomeworkUserById(id);
    // 鉴权
    if (!StringUtils.equals(manageHomeworkUser.getUserId(), SessionUtils.getId())) {
      return CommonCodeEnum.PERMISSION_DENY;
    }
    return CommonCodeEnum.SUCCESS.addData("manageHomeworkUser", manageHomeworkUser);
  }

  //  @GetMapping("/list/{page}/{size}")
  @ApiOperation(value = "分页查询作业提交记录")
  public ResponseResult listManageHomeworkUsersByPage(
      @PathVariable int page,
      @PathVariable int size,
      @Validated @RequestBody(required = false)
          ManageHomeworkUserQueryParam manageHomeworkUserQueryParam) {
    if (page < 1) {
      page = 1;
    }
    if (size <= 0) {
      size = 10;
    }
    if (manageHomeworkUserQueryParam == null) {
      manageHomeworkUserQueryParam = new ManageHomeworkUserQueryParam();
    }
    Page<ManageHomeworkUser> manageHomeworkUserPage =
        manageHomeworkUserService.listManageHomeworkUsersByPage(
            page, size, manageHomeworkUserQueryParam);
    return CommonCodeEnum.SUCCESS.addData(
        "list",
        manageHomeworkUserPage.getContent(),
        "total",
        manageHomeworkUserPage.getTotalElements(),
        "totalPage",
        manageHomeworkUserPage.getTotalPages());
  }

  //  @GetMapping("/list/all")
  @ApiOperation(value = "查询全部作业提交记录")
  public ResponseResult listAllManageHomeworkUsers() {
    List<ManageHomeworkUser> list = manageHomeworkUserService.listAllManageHomeworkUsers();
    return CommonCodeEnum.SUCCESS.addData("list", list, "total", list.size());
  }
}
