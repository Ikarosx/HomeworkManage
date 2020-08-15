package cn.ikarosx.homework.exception;

public class ExceptionCast {
  /** 使用此静态方法抛出自定义异常 */
  public static void cast(ResponseResult resultCode) {
    throw new CustomException(resultCode);
  }
}
