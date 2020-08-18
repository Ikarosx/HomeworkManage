package cn.ikarosx.homework.service.impl;

import cn.ikarosx.homework.entity.ManageHomeworkUser;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.CustomException;
import cn.ikarosx.homework.exception.ExceptionCast;
import cn.ikarosx.homework.model.param.query.ManageHomeworkUserQueryParam;
import cn.ikarosx.homework.repository.ManageHomeworkUserRepository;
import cn.ikarosx.homework.service.ManageHomeworkUserService;
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

/**
 * @author Ikarosx
 * @date 2020/08/17 20:29
 */
@Service
public class ManageHomeworkUserServiceImpl implements ManageHomeworkUserService {

  @Autowired private ManageHomeworkUserRepository manageHomeworkUserRepository;

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
}
