package cn.ikarosx.homework.aspect;

import cn.ikarosx.homework.entity.User;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface IsOwner {
  /** ID的位置 */
  int position() default 0;
  /** 是什么的ID */
  Class clazz() default User.class;
  /** ID字段的名字，首字母大写+驼峰写法 */
  String name() default "Id";
  /** 主键的类型 */
  Class idClazz() default String.class;
}
