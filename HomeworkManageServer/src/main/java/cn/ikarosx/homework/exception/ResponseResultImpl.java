package cn.ikarosx.homework.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/9/18 9:17
 */
@Data
public class ResponseResultImpl implements ResponseResult {

  private boolean success;
  private int code;
  private String message;
  private Map<String, Object> data;

  public ResponseResultImpl(boolean success, int code, String message) {
    this.success = success;
    this.code = code;
    this.message = message;
  }

  @Override
  public boolean getSuccess() {
    return success;
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
