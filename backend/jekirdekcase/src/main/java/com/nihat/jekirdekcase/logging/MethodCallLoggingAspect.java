package com.nihat.jekirdekcase.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodCallLoggingAspect {

    @Before("@within(com.nihat.jekirdekcase.logging.Loggable) && execution(public * *(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info("Entering method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @After("@within(com.nihat.jekirdekcase.logging.Loggable) && execution(public * *(..))")
    public void logMethodExit(JoinPoint joinPoint) {
        log.info("Exiting method: {}", joinPoint.getSignature());
    }
}