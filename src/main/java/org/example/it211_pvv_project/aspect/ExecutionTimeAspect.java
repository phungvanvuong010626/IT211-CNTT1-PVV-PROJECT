package org.example.it211_pvv_project.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//Ghi log thời gian thực hiện của các hàm Service
@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {
    @Around("execution(* org.example.it211_pvv_project.controller..*(..)) || " + "execution(* org.example.it211_pvv_project.service.impl..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("Chức năng [{}] thực hiện trong {} ms", joinPoint.getSignature().toShortString(), (end - start));
        return result;
    }
}