package diy.xiaoming.com.aop.aspectj;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import diy.xiaoming.com.aop.annotation.BehaviorTrace;

/**
 * Created by Administrator on 2018-03-06.
 */

@Aspect
public class BehaviorTraceAspectj {

    @Pointcut("execution(@diy.xiaoming.com.aop.annotation.BehaviorTrace * *(..))")
    public void methodWithBehaviorTrace(){}

    @Around("methodWithBehaviorTrace()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        String funName = signature.getMethod().getAnnotation(BehaviorTrace.class).value();

        Long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        Long duration = System.currentTimeMillis()-begin;
        Log.d("jett",String.format("功能：%s,%s类的%s方法执行了，用时%d ms",funName,className,methodName,duration));
        return result;
    }
}
