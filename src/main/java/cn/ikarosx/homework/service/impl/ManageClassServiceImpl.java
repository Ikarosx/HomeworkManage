package cn.ikarosx.homework.service.impl;

import cn.ikarosx.homework.constant.ManageClassUserStatusEnum;
import cn.ikarosx.homework.entity.ManageClass;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.CustomException;
import cn.ikarosx.homework.exception.ExceptionCast;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.BO.ManageClassDetailInfoUser;
import cn.ikarosx.homework.model.param.insert.ManageClassInsertParam;
import cn.ikarosx.homework.model.param.query.ManageClassQueryParam;
import cn.ikarosx.homework.model.param.update.ManageClassUpdateParam;
import cn.ikarosx.homework.repository.ManageClassRepository;
import cn.ikarosx.homework.repository.UserRepository;
import cn.ikarosx.homework.service.ManageClassService;
import cn.ikarosx.homework.util.SessionUtils;
import cn.ikarosx.homework.util.UpdateUtils;
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
 * @date 2020/08/15 14:10
 */
@Service
public class ManageClassServiceImpl implements ManageClassService {

  @Autowired private ManageClassRepository manageClassRepository;
  @Autowired private UserRepository userRepository;

  @Override
  @Transactional
  public String insertManageClass(ManageClassInsertParam manageClassInsertParam) {
    ManageClass manageClass = new ManageClass();
    BeanUtils.copyProperties(manageClassInsertParam, manageClass);
    manageClass.setAdminUserId(SessionUtils.getId());
    manageClassRepository.save(manageClass);

    return manageClass.getId();
  }

  @Override
  public ResponseResult deleteManageClassById(String id) {
    ManageClass manageClass =
        manageClassRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.PERMISSION_DENY));
    // 如果当前用户不是班级所属管理员
    if (!SessionUtils.isAdmin()
        && !StringUtils.equals(manageClass.getAdminUserId(), SessionUtils.getId())) {
      ExceptionCast.cast(CommonCodeEnum.PERMISSION_DENY);
    }
    manageClassRepository.deleteById(id);
    return CommonCodeEnum.SUCCESS.clearData();
  }

  @Override
  public ResponseResult updateManageClass(ManageClassUpdateParam manageClassUpdateParam) {
    ManageClass manageClass =
        manageClassRepository
            .findById(manageClassUpdateParam.getId())
            .orElseThrow(() -> new CustomException(CommonCodeEnum.PERMISSION_DENY));
    // 如果当前用户不是班级所属管理员
    if (!SessionUtils.isAdmin()
        && !StringUtils.equals(manageClass.getAdminUserId(), SessionUtils.getId())) {
      ExceptionCast.cast(CommonCodeEnum.PERMISSION_DENY);
    }
    UpdateUtils.copyNullProperties(manageClassUpdateParam, manageClass);
    manageClassRepository.save(manageClass);
    return CommonCodeEnum.SUCCESS.clearData();
  }

  @Override
  public ManageClass getManageClassById(String id) {
    ManageClass manageClass =
        manageClassRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    return manageClass;
  }

  @Override
  public ResponseResult listManageClasssByPage(
      int page, int size, ManageClassQueryParam manageClassQueryParam) {
    ManageClass manageClass = new ManageClass();
    BeanUtils.copyProperties(manageClassQueryParam, manageClass);
    // 筛选
    ExampleMatcher matcher =
        ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example<ManageClass> example = Example.of(manageClass, matcher);
    // 分页
    Pageable pageable = PageRequest.of(page - 1, size);
    Page<ManageClass> manageClassPage = manageClassRepository.findAll(example, pageable);
    return CommonCodeEnum.SUCCESS.addData(
        "list",
        manageClassPage.getContent(),
        "total",
        manageClassPage.getTotalElements(),
        "totalPage",
        manageClassPage.getTotalPages());
  }

  @Override
  public ResponseResult listAllManageClasss() {
    List<ManageClass> list = manageClassRepository.findAll();
    return CommonCodeEnum.SUCCESS.addData("list", list, "total", list.size());
  }

  @Override
  public List<ManageClassDetailInfoUser> listAllManageClassUser(String classId, Integer status) {
    // 必须同班才能查询
    if (!StringUtils.equals(SessionUtils.getClassId(), classId)) {
      ExceptionCast.cast(CommonCodeEnum.PERMISSION_DENY);
    }
    ManageClass manageClass =
        manageClassRepository
            .findById(classId)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    // 如果要查询除了通过以外的的，必须是管理员
    if (!ManageClassUserStatusEnum.Joined.getStatus().equals(status)
        && !StringUtils.equals(manageClass.getAdminUserId(), SessionUtils.getId())) {
      ExceptionCast.cast(CommonCodeEnum.PERMISSION_DENY);
    }
    List<ManageClassDetailInfoUser> users =
        manageClassRepository.findAllByClassIdAndStatus(classId, status);
    return users;
  }
}
