package cn.ikarosx.homework.model.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelAnnotation {

  /** 列索引 */
  int columnIndex() default 0;

  /** 列名 */
  String columnName() default "";
}
