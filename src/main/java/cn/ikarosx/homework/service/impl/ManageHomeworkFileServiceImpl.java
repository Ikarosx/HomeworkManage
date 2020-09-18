package cn.ikarosx.homework.service.impl;

import cn.ikarosx.homework.entity.ManageHomeworkFile;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.ExceptionCast;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.insert.ManageHomeworkFileInsertParam;
import cn.ikarosx.homework.model.param.query.ManageHomeworkFileQueryParam;
import cn.ikarosx.homework.model.param.update.ManageHomeworkFileUpdateParam;
import cn.ikarosx.homework.repository.ManageHomeworkFileRepository;
import cn.ikarosx.homework.service.ManageHomeworkFileService;
import java.util.List;
import java.util.Optional;
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
 * @date 2020/09/16 21:40
 */
@Service
public class ManageHomeworkFileServiceImpl implements ManageHomeworkFileService {

  @Autowired private ManageHomeworkFileRepository manageHomeworkFileRepository;

  @Override
  public ResponseResult insertManageHomeworkFile(
      ManageHomeworkFileInsertParam manageHomeworkFileInsertParam) {
    ManageHomeworkFile manageHomeworkFile = new ManageHomeworkFile();
    BeanUtils.copyProperties(manageHomeworkFileInsertParam, manageHomeworkFile);
    manageHomeworkFileRepository.save(manageHomeworkFile);
    return CommonCodeEnum.SUCCESS;
  }

  @Override
  public ResponseResult deleteManageHomeworkFileById(String id) {
    manageHomeworkFileRepository.deleteById(id);
    return CommonCodeEnum.SUCCESS;
  }

  @Override
  public ResponseResult updateManageHomeworkFile(
      ManageHomeworkFileUpdateParam manageHomeworkFileUpdateParam) {
    ManageHomeworkFile manageHomeworkFile = new ManageHomeworkFile();
    BeanUtils.copyProperties(manageHomeworkFileUpdateParam, manageHomeworkFile);
    manageHomeworkFileRepository.save(manageHomeworkFile);
    return CommonCodeEnum.SUCCESS;
  }

  @Override
  public ResponseResult getManageHomeworkFileById(String id) {
    Optional<ManageHomeworkFile> optional = manageHomeworkFileRepository.findById(id);
    ManageHomeworkFile manageHomeworkFile = optional.orElse(null);
    if (manageHomeworkFile == null) {
      ExceptionCast.cast(CommonCodeEnum.DATA_NOT_FOUND);
    }
    return CommonCodeEnum.SUCCESS.addData("manageHomeworkFile", manageHomeworkFile);
  }

  @Override
  public ResponseResult listManageHomeworkFilesByPage(
      int page, int size, ManageHomeworkFileQueryParam manageHomeworkFileQueryParam) {
    ManageHomeworkFile manageHomeworkFile = new ManageHomeworkFile();
    BeanUtils.copyProperties(manageHomeworkFileQueryParam, manageHomeworkFile);
    // 筛选
    ExampleMatcher matcher =
        ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example<ManageHomeworkFile> example = Example.of(manageHomeworkFile, matcher);
    // 分页
    Pageable pageable = PageRequest.of(page - 1, size);
    Page<ManageHomeworkFile> manageHomeworkFilePage =
        manageHomeworkFileRepository.findAll(example, pageable);
    return CommonCodeEnum.SUCCESS.addData(
        "list",
        manageHomeworkFilePage.getContent(),
        "total",
        manageHomeworkFilePage.getTotalElements(),
        "totalPage",
        manageHomeworkFilePage.getTotalPages());
  }

  @Override
  public ResponseResult listAllManageHomeworkFiles() {
    List<ManageHomeworkFile> list = manageHomeworkFileRepository.findAll();
    return CommonCodeEnum.SUCCESS.addData("list", list, "total", list.size());
  }
}
