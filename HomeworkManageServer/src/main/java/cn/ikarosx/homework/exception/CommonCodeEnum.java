package cn.ikarosx.homework.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ikarosx
 * @date 2020/1/26 16:58
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommonCodeEnum implements ResponseResult {

  /** 通用错误代码 10000 */
  SUCCESS(true, 10000, "操作成功！"),
  INVALID_PARAM(false, 10001, "非法参数"),
  DATA_INTEGRITY_VIOLATION_EXCEPTION(false, 10002, "数据完整性约束异常"),
  JSON_PARSE_ERROR0(false, 10003, "json解析失败"),
  FILE_SIZE_LIMIT_EXCEEDED(false, 10004, "上传文件过大"),
  IO_EXCEPTION(false, 10005, "IO异常"),
  DATA_NOT_FOUND(false, 10006, "数据不存在"),
  HTTP_REQUEST_METHOD_NOT_SUPPORT_EXCEPTION(false, 10007, "HTTP请求方法不支持"),
  DATA_EXIST_ERROR(false, 10008, "数据已存在"),
  EXCEL_GET_FIELD_ERROR(false, 10009, "EXCEL获取字段时获取不到"),
  EXCEL_GET_ANNOTATION_ERROR(false, 10010, "EXCEL获取注解时获取不到"),
  FAIL(false, 11111, "操作失败！"),
  SERVER_ERROR(false, 99999, "系统繁忙，请稍后重试！"),
  /** 用户相关 11000 */
  USERNAME_OR_PASSWORD_ERROR(false, 11001, "用户名或密码错误"),
  RE_LOGIN(false, 11002, "请重新登陆"),
  PERMISSION_DENY(false, 11003, "没有操作权限"),
  USERNAME_EXISTS_ERROR(false, 11004, "用户名已存在");
  private boolean success;
  private int code;
  private String message;
  private Map<String, Object> data;

  CommonCodeEnum(boolean success, int code, String message) {
    this.success = success;
    this.code = code;
    this.message = message;
  }

  CommonCodeEnum(boolean success, int code, String message, Map<String, Object> data) {
    this.success = success;
    this.code = code;
    this.message = message;
    this.data = data;
  }

  @Override
  public boolean getSuccess() {
    return success;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public Map getData() {
    return data;
  }

  @Override
  public ResponseResult addData(Object... objects) {
    data = new HashMap<>();
    assert (objects.length & 1) == 0;
    for (int i = 0; i < objects.length; i++) {
      data.put((String) objects[i], objects[++i]);
    }
    return this;
  }
}
