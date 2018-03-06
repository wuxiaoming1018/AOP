package diy.xiaoming.com.aop.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Administrator on 2018-03-06.
 */

@Aspect
public class BeforeBehaviorTraceAspectj {

    @Pointcut("execution(@diy.xiaoming.com.aop.annotation.BeforeBehavior * *(..))")
    public void useBeforeAnnotation(){}

    @Before("useBeforeAnnotation()")
    public Object methodwithBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        return result;
    }
}
