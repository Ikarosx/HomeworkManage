package cn.ikarosx.homework.aspect;

import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.ExceptionCast;
import cn.ikarosx.homework.util.SessionUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 处理IsOwner注解
 *
 * @author Ikarosx
 * @date 2020/7/9 13:52
 */
@Aspect
@Slf4j
@Component
public class OwnerAspect {
  @Pointcut("@annotation(IsOwner)")
  public void ownerPointCut() {}

  @Before("ownerPointCut()")
  public void ownerBefore(JoinPoint joinPoint) {
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();
    // 拿到IsOwner注解
    IsOwner isOwner = method.getAnnotation(IsOwner.class);
    // 拿到要校验参数的位置
    int position = isOwner.position();
    // 拿到目标参数的类型
    Class clazz = isOwner.clazz();
    // 拿到主键的类型
    Class idClazz = isOwner.idClazz();
    // 拿到字段名称
    String name = isOwner.name();
    Object[] args = joinPoint.getArgs();
    if (args == null || args.length == 0) {
      log.warn("标记了IsOwner但参数为空：" + method.getName());
      ExceptionCast.cast(CommonCodeEnum.FAIL);
    }
    if (position > args.length) {
      log.warn("标记了IsOwner但position不对：" + method.getName());
      ExceptionCast.cast(CommonCodeEnum.FAIL);
    }
    Object arg = args[position];
    Object id = null;
    try {
      id = idClazz.newInstance();
      if (arg.getClass() == idClazz) {
        // 如果是该类型刚好是主键类型，直接强转
        id = idClazz.cast(arg);
      } else if (arg.getClass() == clazz) {
        // 如果目标值的类型等于填写的类型，通过反射拿到get方法的值
        Method declaredMethod = clazz.getDeclaredMethod("get" + name);
        id = declaredMethod.invoke(arg);
      } else {
        log.warn("标记了IsOwner但目标参数既不是String也不是指定的class类型：" + method.getName());
        ExceptionCast.cast(CommonCodeEnum.FAIL);
      }
    } catch (InstantiationException
        | InvocationTargetException
        | NoSuchMethodException
        | IllegalAccessException e) {
      log.error("触发异常", e);
      ExceptionCast.cast(CommonCodeEnum.FAIL);
    }
    String userId = SessionUtils.getUser().getId();
    if (id == null || !StringUtils.equals(id.toString(), userId)) {
      log.warn(id + "没有对该资源的操作权限：" + userId);
      ExceptionCast.cast(CommonCodeEnum.PERMISSION_DENY);
    }
  }
}
