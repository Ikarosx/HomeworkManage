package cn.ikarosx.homework.exception;

import cn.ikarosx.homework.model.ResponseResultDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

/**
 * @author Ikarosx
 * @date 2020/1/26 16:59
 */
@JsonDeserialize(using = ResponseResultDeserialize.class)
public interface ResponseResult {
  /** @return 操作是否成功, true为成功，false操作失败 */
  boolean getSuccess();

  /**
   * 10000-- 通用错误代码
   *
   * @return 操作代码
   */
  int getCode();

  /** @return 提示信息 */
  String getMessage();

  Map getData();

  ResponseResult addData(Object... objects);
}
