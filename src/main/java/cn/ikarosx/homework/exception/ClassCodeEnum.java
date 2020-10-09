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
  CLASS_MEMBER_CAN_NOT_CREATE_CLASS(false, 12001, "已经为班级成员无法添加班级"),
  ADMIN_NOT_FOUND_ERROR(false, 12002, "管理员数据无法找到"),
  CLASS_MEMBER_CAN_NOT_JSON_CLASS(false, 12003, "你已经加入了一个班级，无法加入另一个"),
  CLASS_NOT_FOUNT_ERROR(false, 12004, "班级不存在"),
  WAITING_ADMIN_AGREE(false, 12005, "你已经申请加入班级，请等待管理员同意"),
  AGREE_ERROR(false, 12006, "该成员已经加入其他班级"),
  USER_NAME_EXIST_ERROR(false, 12007, "用户名已存在");

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
