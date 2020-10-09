package cn.ikarosx.homework.aspect;

import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.ExceptionCast;
import cn.ikarosx.homework.util.SessionUtils;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author Ikarosx
 * @date 2020/7/9 13:52
 */
@Aspect
@Slf4j
@Component
public class AdminAspect {
  @Pointcut("@annotation(NeedAdmin)")
  public void needAdminPointCut() {}

  @Before("needAdminPointCut()()")
  public void needAdminBefore(JoinPoint joinPoint) {
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();
    if (!SessionUtils.isAdmin()) {
      log.error("此方法需要管理员权限：" + method.getName());
      ExceptionCast.cast(CommonCodeEnum.PERMISSION_DENY);
    }
  }
}
