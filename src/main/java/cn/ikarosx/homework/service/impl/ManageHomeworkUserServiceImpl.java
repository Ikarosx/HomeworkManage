package cn.ikarosx.homework.service.impl;

import cn.ikarosx.homework.entity.ManageHomework;
import cn.ikarosx.homework.entity.ManageHomeworkFile;
import cn.ikarosx.homework.entity.ManageHomeworkUser;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.CustomException;
import cn.ikarosx.homework.exception.ExceptionCast;
import cn.ikarosx.homework.model.param.insert.ManageHomeworkUserInsertParam;
import cn.ikarosx.homework.model.param.query.ManageHomeworkUserQueryParam;
import cn.ikarosx.homework.repository.ManageHomeworkFileRepository;
import cn.ikarosx.homework.repository.ManageHomeworkUserRepository;
import cn.ikarosx.homework.service.ManageHomeworkService;
import cn.ikarosx.homework.service.ManageHomeworkUserService;
import cn.ikarosx.homework.util.SessionUtils;
import cn.ikarosx.homework.util.UpdateUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ikarosx
 * @date 2020/08/17 20:29
 */
@Service
@Slf4j
public class ManageHomeworkUserServiceImpl implements ManageHomeworkUserService {

  @Autowired private ManageHomeworkUserRepository manageHomeworkUserRepository;
  @Autowired private ManageHomeworkService manageHomeworkService;
  @Autowired private ManageHomeworkFileRepository manageHomeworkFileRepository;
  @Autowired private RestTemplate restTemplate;

  @Value("${fdfsUrl}")
  private String fdfsUrl;

  @Override
  public void insertManageHomeworkUser(ManageHomeworkUser manageHomeworkUser) {
    ManageHomeworkUser manageHomeworkUser1 = new ManageHomeworkUser();
    manageHomeworkUser1.setUserId(manageHomeworkUser.getUserId());
    manageHomeworkUser1.setHomeworkId(manageHomeworkUser.getHomeworkId());

    manageHomeworkUser1 =
        manageHomeworkUserRepository.findOne(Example.of(manageHomeworkUser1)).orElse(null);
    if (manageHomeworkUser1 != null) {
      ExceptionCast.cast(CommonCodeEnum.DATA_EXIST_ERROR);
    }
    manageHomeworkUserRepository.save(manageHomeworkUser);
  }

  @Override
  public void deleteManageHomeworkUserById(String id) {
    manageHomeworkUserRepository.deleteById(id);
  }

  @Override
  public void updateManageHomeworkUser(ManageHomeworkUser manageHomeworkUser) {
    ManageHomeworkUser orgManageHomeworkUser =
        manageHomeworkUserRepository
            .findById(manageHomeworkUser.getId())
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    // 鉴权
    if (!StringUtils.equals(orgManageHomeworkUser.getUserId(), SessionUtils.getId())) {
      ExceptionCast.cast(CommonCodeEnum.PERMISSION_DENY);
    }
    UpdateUtils.copyNullProperties(manageHomeworkUser, orgManageHomeworkUser);
    manageHomeworkUserRepository.save(orgManageHomeworkUser);
  }

  @Override
  public ManageHomeworkUser getManageHomeworkUserById(String id) {
    ManageHomeworkUser manageHomeworkUser =
        manageHomeworkUserRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    return manageHomeworkUser;
  }

  @Override
  public Page<ManageHomeworkUser> listManageHomeworkUsersByPage(
      int page, int size, ManageHomeworkUserQueryParam manageHomeworkUserQueryParam) {
    ManageHomeworkUser manageHomeworkUser = new ManageHomeworkUser();
    BeanUtils.copyProperties(manageHomeworkUserQueryParam, manageHomeworkUser);
    // 筛选
    ExampleMatcher matcher =
        ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example<ManageHomeworkUser> example = Example.of(manageHomeworkUser, matcher);
    // 分页
    Pageable pageable = PageRequest.of(page - 1, size);
    Page<ManageHomeworkUser> manageHomeworkUserPage =
        manageHomeworkUserRepository.findAll(example, pageable);
    return manageHomeworkUserPage;
  }

  @Override
  public List<ManageHomeworkUser> listAllManageHomeworkUsers() {
    List<ManageHomeworkUser> list = manageHomeworkUserRepository.findAll();
    return list;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public String submitHomework(ManageHomeworkUserInsertParam manageHomeworkUserInsertParam) {
    // fileIds
    String fileIds = manageHomeworkUserInsertParam.getFileIds();
    // 通过homeworkId找出作业记录
    ManageHomework manageHomework =
        manageHomeworkService.getManageHomeworkById(manageHomeworkUserInsertParam.getHomeworkId());
    // 保证要提交作业的班级是自己所属班级
    if (!StringUtils.equals(manageHomework.getClassId(), SessionUtils.getClassId())) {
      log.warn("用户{}正在非法提交作业", SessionUtils.getId());
      throw new CustomException(
          CommonCodeEnum.PERMISSION_DENY.addData("errorMsg", "请不要非法提交不属于你的班级"));
    }

    ManageHomeworkUser manageHomeworkUser = new ManageHomeworkUser();
    BeanUtils.copyProperties(manageHomeworkUserInsertParam, manageHomeworkUser);
    // 设置为已经提交
    manageHomeworkUser.setStatus(1);
    manageHomeworkUser.setUserId(SessionUtils.getId());
    insertManageHomeworkUser(manageHomeworkUser);
    // 插入作业对应文件，如何判断文件属于该用户
    Boolean hasPermission =
        restTemplate.getForObject(
            fdfsUrl + "/fileSystem/owner?userId=" + SessionUtils.getId() + "&fileIds=" + fileIds + "&businessKey=homework",
            Boolean.class);
    if (hasPermission == null || !hasPermission) {
      // 如果没有权限
      log.warn("用户{}没有权限操作文件列表中的某些文件：{}", SessionUtils.getId(), fileIds);
      throw new CustomException(
          CommonCodeEnum.PERMISSION_DENY.addData("errorMsg", "无权操作提交列表中的某些文件"));
    }
    // 插入作业记录表
    manageHomeworkUserRepository.save(manageHomeworkUser);
    // 构建manageHomeworkFile列表
    List<ManageHomeworkFile> manageHomeworkFileList =
        Arrays.stream(fileIds.split(","))
            .distinct()
            .map(
                fileId -> {
                  ManageHomeworkFile manageHomeworkFile = new ManageHomeworkFile();
                  manageHomeworkFile.setFileId(fileId);
                  manageHomeworkFile.setHomeworkUserId(manageHomeworkUser.getId());
                  return manageHomeworkFile;
                })
            .collect(Collectors.toList());
    // 批量插入
    manageHomeworkFileRepository.saveAll(manageHomeworkFileList);
    // 返回提交作业记录ID
    return manageHomeworkUser.getId();
  }
}
