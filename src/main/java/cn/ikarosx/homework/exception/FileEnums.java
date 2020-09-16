package cn.ikarosx.homework.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ikarosx
 * @date 2020/9/16 9:32
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FileEnums implements ResponseResult {
  /** 相关文件相关 12000 */
  UPLOAD_FILE_ERROR(true, 12001, "上传文件失败");

  private boolean success;
  private int code;
  private String message;
  private Map<String, Object> data;

  FileEnums(boolean success, int code, String message) {
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
