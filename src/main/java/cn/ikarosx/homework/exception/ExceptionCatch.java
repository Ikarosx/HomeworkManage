package cn.ikarosx.homework.exception;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @author Ikarosx
 * @date 2020/1/26 18:24
 */
@RestControllerAdvice
@Slf4j
public class ExceptionCatch {

  /** 使用EXCEPTIONS存放异常类型和错误代码的映射， ImmutableMap的特点的一旦创建不可改变，并且线程安全 */
  private static ImmutableMap<Class<? extends Throwable>, ResponseResult> EXCEPTIONS;

  /** 使用builder来构建一个异常类型和错误代码的异常 */
  private static ImmutableMap.Builder<Class<? extends Throwable>, ResponseResult> builder =
      ImmutableMap.builder();

  static {
    // 在这里加入一些基础的异常类型判断
    builder.put(HttpMessageNotReadableException.class, CommonCodeEnum.INVALID_PARAM);
    builder.put(
        DataIntegrityViolationException.class, CommonCodeEnum.DATA_INTEGRITY_VIOLATION_EXCEPTION);
    builder.put(MethodArgumentNotValidException.class, CommonCodeEnum.INVALID_PARAM);
    builder.put(AccessDeniedException.class, CommonCodeEnum.PERMISSION_DENY);
    builder.put(
        HttpRequestMethodNotSupportedException.class,
        CommonCodeEnum.HTTP_REQUEST_METHOD_NOT_SUPPORT_EXCEPTION);
    builder.put(MaxUploadSizeExceededException.class, CommonCodeEnum.FILE_SIZE_LIMIT_EXCEEDED);
  }

  @ExceptionHandler(CustomException.class)
  public ResponseResult customException(CustomException e) {
    log.error("catch exception : {}", e.getMessage());
    return e.getResponseResult();
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseResult customException(EmptyResultDataAccessException e) {
    log.error("catch exception : {}", e.getMessage());
    // DAO访问数据为空
    // TODO
    return CommonCodeEnum.SUCCESS;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();
    log.error("catch exception : {}", e.getMessage());
    return convertFieldErrors(bindingResult.getAllErrors());
  }

  @ExceptionHandler(BindException.class)
  @ResponseBody
  public ResponseResult bindException(BindException e) {
    log.error("catch exception : {}", e.getMessage());
    List<ObjectError> allErrors = e.getAllErrors();
    return convertFieldErrors(allErrors);
  }

  private ResponseResult convertFieldErrors(List<ObjectError> allErrors) {
    List<JSONObject> validationMessages = new ArrayList<>();
    for (ObjectError objectError : allErrors) {
      JSONObject jsonObject = new JSONObject();
      if (objectError instanceof FieldError) {
        FieldError fieldError = (FieldError) objectError;
        jsonObject.put("字段", fieldError.getField());
        jsonObject.put("信息", fieldError.getDefaultMessage());
      } else {
        jsonObject.put(null, objectError.getDefaultMessage());
      }
      validationMessages.add(jsonObject);
    }
    return CommonCodeEnum.INVALID_PARAM.addData("errors", validationMessages);
  }

  @ExceptionHandler(Exception.class)
  public ResponseResult exception(Exception exception) {
    // 记录日志
    exception.printStackTrace();
    log.error("catch exception:{}", exception.getClass() + "------" + exception.getMessage());
    if (EXCEPTIONS == null) {
      EXCEPTIONS = builder.build();
    }
    final ResponseResult resultCode = EXCEPTIONS.get(exception.getClass());
    final ResponseResult responseResult;
    if (resultCode != null) {
      responseResult = resultCode;
    } else {
      responseResult = CommonCodeEnum.SERVER_ERROR;
    }
    return responseResult;
  }
}
