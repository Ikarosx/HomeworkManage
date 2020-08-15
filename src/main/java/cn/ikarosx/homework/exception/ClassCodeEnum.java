package cn.ikarosx.homework.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ikarosx
 * @date 2020/1/26 16:58
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ClassCodeEnum implements ResponseResult {

  /** 班级相关 12000 */
  CLASS_MEMBER_CAN_NOT_CREATE_CLASS(false, 12001, "已经为班级成员无法添加班级");
  private boolean success;
  private int code;
  private String message;
  private Map<String, Object> data;

  ClassCodeEnum(boolean success, int code, String message) {
    this.success = success;
    this.code = code;
    this.message = message;
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

  public ResponseResult clearData() {
    data = null;
    return this;
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
