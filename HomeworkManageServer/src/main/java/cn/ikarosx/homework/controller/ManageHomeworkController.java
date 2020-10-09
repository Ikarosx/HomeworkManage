package cn.ikarosx.homework.controller;

import cn.ikarosx.homework.aspect.NeedAdmin;
import cn.ikarosx.homework.aspect.PreAuthorize;
import cn.ikarosx.homework.entity.ManageHomework;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.ExceptionCast;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.BO.HomeworkFinishInfo;
import cn.ikarosx.homework.model.BO.ManageHomeworkDetails;
import cn.ikarosx.homework.model.excel.HomeworkFinishInfoExcelModel;
import cn.ikarosx.homework.model.param.insert.ManageHomeworkInsertParam;
import cn.ikarosx.homework.model.param.query.ManageHomeworkQueryParam;
import cn.ikarosx.homework.model.param.update.ManageHomeworkUpdateParam;
import cn.ikarosx.homework.service.ManageHomeworkService;
import cn.ikarosx.homework.util.ExcelUtils;
import cn.ikarosx.homework.util.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Ikarosx
 * @date 2020/08/17 14:16
 */
@RestController
@RequestMapping("manageHomework")
@Api(tags = "作业")
public class ManageHomeworkController {
  @Autowired private ManageHomeworkService manageHomeworkService;

  @PostMapping
  @ApiOperation(value = "新增作业", notes = "班级/系统管理员可以添加作业")
  @PreAuthorize(
      "@userServiceImpl.isAdmin() || @userServiceImpl.isClassOwner(#manageHomeworkInsertParam.classId)")
  public ResponseResult insertManageHomework(
      @Validated @RequestBody ManageHomeworkInsertParam manageHomeworkInsertParam) {
    // TODO 增加科目
    ManageHomework manageHomework = new ManageHomework();
    BeanUtils.copyProperties(manageHomeworkInsertParam, manageHomework);
    manageHomeworkService.insertManageHomework(manageHomework);
    return CommonCodeEnum.SUCCESS.addData("manageHomeWorkId", manageHomework.getId());
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "通过ID删除作业", notes = "班级/系统管理员可以删除作业")
  @PreAuthorize(
      "@userServiceImpl.isAdmin() || @manageHomeworkServiceImpl.hasPermissionOnHomeworkByHomeworkId(#id)")
  public ResponseResult deleteManageHomeworkById(@PathVariable String id) {
    manageHomeworkService.deleteManageHomeworkById(id);
    return CommonCodeEnum.SUCCESS;
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "更新作业")
  @PreAuthorize(
      "@userServiceImpl.isAdmin() || @manageHomeworkServiceImpl.hasPermissionOnHomeworkByHomeworkId(#manageHomeworkUpdateParam.id)")
  public ResponseResult updateManageHomework(
      @PathVariable String id,
      @Validated @RequestBody ManageHomeworkUpdateParam manageHomeworkUpdateParam) {
    ManageHomework manageHomework = new ManageHomework();
    BeanUtils.copyProperties(manageHomeworkUpdateParam, manageHomework);
    manageHomeworkService.updateManageHomework(manageHomework);
    return CommonCodeEnum.SUCCESS;
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "通过ID查询作业", notes = "班级成员和系统管理员可以查看")
  public ResponseResult getManageHomeworkById(@PathVariable String id) {
    ManageHomework manageHomework = manageHomeworkService.getManageHomeworkById(id);
    // 不是系统管理员也不是班级成员
    if (!SessionUtils.isAdmin()
        && !StringUtils.equals(manageHomework.getClassId(), SessionUtils.getClassId())) {
      return CommonCodeEnum.PERMISSION_DENY;
    }
    return CommonCodeEnum.SUCCESS.addData("manageHomework", manageHomework);
  }

  //  @GetMapping("/list/{page}/{size}")
  @ApiOperation(value = "分页查询作业")
  public ResponseResult listManageHomeworksByPage(
      @PathVariable int page,
      @PathVariable int size,
      @Validated @RequestBody(required = false) ManageHomeworkQueryParam manageHomeworkQueryParam) {
    if (page < 1) {
      page = 1;
    }
    if (size <= 0) {
      size = 10;
    }
    if (manageHomeworkQueryParam == null) {
      manageHomeworkQueryParam = new ManageHomeworkQueryParam();
    }
    Page<ManageHomework> manageHomeworkPage =
        manageHomeworkService.listManageHomeworksByPage(page, size, manageHomeworkQueryParam);
    return CommonCodeEnum.SUCCESS.addData(
        "list",
        manageHomeworkPage.getContent(),
        "total",
        manageHomeworkPage.getTotalElements(),
        "totalPage",
        manageHomeworkPage.getTotalPages());
  }

  @GetMapping("/list/all")
  @ApiOperation(value = "查询全部作业")
  @NeedAdmin
  public ResponseResult listAllManageHomeworks() {
    List<ManageHomework> list = manageHomeworkService.listAllManageHomeworks();
    return CommonCodeEnum.SUCCESS.addData("list", list, "total", list.size());
  }

  @GetMapping("/current/list/all")
  @ApiOperation(value = "查询班级全部作业")
  public ResponseResult listAllManageHomeworksByCurrentUser() {
    List<ManageHomeworkDetails> list = manageHomeworkService.listAllManageHomeworksByCurrentUser();
    return CommonCodeEnum.SUCCESS.addData("list", list, "total", list.size());
  }

  @GetMapping("/{id}/finishinfo")
  @ApiOperation(value = "查询某个作业的完成情况", notes = "只有班级成员可以查看")
  public ResponseResult getHomeworkFinishInfo(
      @PathVariable String id, @RequestParam String classId) {
    // 只有班级成员可以查看
    if (!StringUtils.equals(classId, SessionUtils.getClassId())) {
      return CommonCodeEnum.PERMISSION_DENY;
    }
    List<HomeworkFinishInfo> list = manageHomeworkService.getHomeworkFinishInfo(classId, id);
    return CommonCodeEnum.SUCCESS.addData("list", list, "total", list.size());
  }

  @GetMapping("/{id}/finishinfo/download")
  @ApiOperation(value = "下载某个作业的完成情况", notes = "只有班级成员可以下载")
  public void downloadHomeworkFinishInfo(@PathVariable String id, @RequestParam String classId) {
    // 只有班级成员可以下载
    if (!StringUtils.equals(classId, SessionUtils.getClassId())) {
      ExceptionCast.cast(CommonCodeEnum.PERMISSION_DENY);
    }
    List<HomeworkFinishInfo> homeworkFinishInfos =
        manageHomeworkService.getHomeworkFinishInfo(classId, id);
    List<HomeworkFinishInfoExcelModel> datas =
        homeworkFinishInfos
            .parallelStream()
            .map(
                homeworkFinishInfo -> {
                  HomeworkFinishInfoExcelModel homeworkFinishInfoExcelModel =
                      new HomeworkFinishInfoExcelModel();
                  BeanUtils.copyProperties(homeworkFinishInfo, homeworkFinishInfoExcelModel);
                  Integer status = homeworkFinishInfo.getStatus();
                  if (status != null && status.equals(1)) {
                    homeworkFinishInfoExcelModel.setStatus("是");
                  } else {
                    homeworkFinishInfoExcelModel.setStatus("否");
                  }
                  return homeworkFinishInfoExcelModel;
                })
            .collect(Collectors.toList());
    ServletRequestAttributes requestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    ExcelUtils.exportExcel(
        requestAttributes.getResponse(),
        "excel/HomeworkFinishInfoTemplate.xlsx",
        HomeworkFinishInfoExcelModel.class,
        datas);
  }
}
