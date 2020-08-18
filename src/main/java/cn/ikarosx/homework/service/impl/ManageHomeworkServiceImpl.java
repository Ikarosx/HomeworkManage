package cn.ikarosx.homework.service.impl;

import cn.ikarosx.homework.entity.ManageHomework;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.CustomException;
import cn.ikarosx.homework.model.BO.ManageHomeworkDetails;
import cn.ikarosx.homework.model.param.query.ManageHomeworkQueryParam;
import cn.ikarosx.homework.repository.ManageHomeworkRepository;
import cn.ikarosx.homework.service.ManageHomeworkService;
import cn.ikarosx.homework.util.SessionUtils;
import cn.ikarosx.homework.util.UpdateUtils;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
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
 * @date 2020/08/17 14:16
 */
@Service
public class ManageHomeworkServiceImpl implements ManageHomeworkService {

  @Autowired private ManageHomeworkRepository manageHomeworkRepository;

  @Override
  public void insertManageHomework(ManageHomework manageHomework) {
    manageHomeworkRepository.save(manageHomework);
  }

  @Override
  public void deleteManageHomeworkById(String id) {
    manageHomeworkRepository.deleteById(id);
  }

  @Override
  public void updateManageHomework(ManageHomework manageHomework) {
    ManageHomework orgManageHomework =
        manageHomeworkRepository
            .findById(manageHomework.getId())
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    UpdateUtils.copyNullProperties(manageHomework, orgManageHomework);
    manageHomeworkRepository.save(orgManageHomework);
  }

  @Override
  public ManageHomework getManageHomeworkById(String id) {
    ManageHomework manageHomework =
        manageHomeworkRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(CommonCodeEnum.DATA_NOT_FOUND));
    return manageHomework;
  }

  @Override
  public Page<ManageHomework> listManageHomeworksByPage(
      int page, int size, ManageHomeworkQueryParam manageHomeworkQueryParam) {
    ManageHomework manageHomework = new ManageHomework();
    BeanUtils.copyProperties(manageHomeworkQueryParam, manageHomework);
    // 筛选
    ExampleMatcher matcher =
        ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example<ManageHomework> example = Example.of(manageHomework, matcher);
    // 分页
    Pageable pageable = PageRequest.of(page - 1, size);
    Page<ManageHomework> manageHomeworkPage = manageHomeworkRepository.findAll(example, pageable);
    return manageHomeworkPage;
  }

  @Override
  public List<ManageHomework> listAllManageHomeworks() {
    List<ManageHomework> list = manageHomeworkRepository.findAll();
    return list;
  }

  /**
   * 判断当前用户是否有权限删除作业
   *
   * @param homeworkId
   * @return
   */
  @Override
  public boolean hasPermissionOnHomeworkByHomeworkId(String homeworkId) {
    String adminUserId = manageHomeworkRepository.getAdminUserIdByHomeworkId(homeworkId);
    return StringUtils.equals(adminUserId, SessionUtils.getId());
  }

  @Override
  public List<ManageHomeworkDetails> listAllManageHomeworksByCurrentUser() {
    List<ManageHomeworkDetails> list =
        manageHomeworkRepository.listAllManageHomeworksByCurrentUser(
            SessionUtils.getId(), SessionUtils.getClassId());
    // 排序 先未完成，后超时，后已完成
    list =
        list.parallelStream()
            .sorted(
                (o1, o2) -> {
                  // 未完成的排在前面，要小数
                  if (o1.getFinish() && !o2.getFinish()) {
                    return 1;
                  } else if (!o1.getFinish() && o2.getFinish()) {
                    return -1;
                  }
                  // 过期时间 - 当前时间
                  // 正数：越小排越前
                  // 负数：说明已经超时，比较创建时间
                  // 拿到当前时间
                  LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
                  LocalDateTime o1LocalDateTime =
                      o1.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                  LocalDateTime o2LocalDateTime =
                      o2.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                  // 拿到
                  Duration o1Between = Duration.between(o1LocalDateTime, now);
                  Duration o2Between = Duration.between(o2LocalDateTime, now);
                  // 两者都超时,比较创建时间
                  if (o1Between.isNegative() && o2Between.isNegative()) {
                    return o1.getCreateTime().compareTo(o2.getCreateTime());
                  }
                  // o1超时
                  if (o1Between.isNegative()) {
                    return -1;
                  }
                  // o2超时
                  if (o2Between.isNegative()) {
                    return 1;
                  }
                  // 两者都不超时,时间越小，说明越紧急，要在前面
                  return o1Between.compareTo(o2Between);
                })
            .collect(Collectors.toList());
    return list;
  }
}
