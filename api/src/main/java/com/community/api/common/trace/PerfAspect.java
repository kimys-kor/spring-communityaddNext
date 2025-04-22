package com.community.api.common.trace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class PerfAspect {

    private final LogTrace logTrace;

    @Pointcut("execution(* com.oneday.api..*Controller*.*(..))")
    public void allService(){};

    @Pointcut("execution(* com.oneday.api..*Service*.*(..))")
    public void allRepository(){};

    @Pointcut("execution(* com.oneday.api..*Repository*.*(..))")
    public void allController(){};

    @Around("allService() || allController() || allRepository()")
    public Object logTrace(ProceedingJoinPoint joinPoint) throws Throwable {

        TraceStatus status = null;

        try{

            status = logTrace.begin(joinPoint.getSignature().toShortString());
            Object result = joinPoint.proceed();

            logTrace.end(status);

            return result;
        }catch (Throwable e){
            e.printStackTrace();
            logTrace.exception(status, e);
            throw e;
        }
    }




}