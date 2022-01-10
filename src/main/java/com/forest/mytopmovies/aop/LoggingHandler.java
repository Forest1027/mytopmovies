package com.forest.mytopmovies.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LoggingHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHandler.class);

    @Before("execution(* com.forest.mytopmovies.controller.*.*.*(..))")
    public void logRequests(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        try {
            HttpServletRequest request = attributes.getRequest();
            LOGGER.debug(String.format("REST request ===> %s  %s, in class ===> %s with parameter ===> %s", request.getMethod(),
                    request.getRequestURL(), joinPoint.getSignature().getDeclaringTypeName(), Arrays.toString(joinPoint.getArgs())));
        } catch (NullPointerException exception) {
            LOGGER.debug(String.format("REST request (CAN'T GET INFO), in class ===> %s with parameter ===> %s", joinPoint.getSignature().getDeclaringTypeName(), Arrays.toString(joinPoint.getArgs())));
        }

    }

    @AfterReturning(pointcut = "execution(* com.forest.mytopmovies.controller.*.*.*(..))", returning = "result")
    public void logResponse(Object result) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            LOGGER.debug(String.format("REST response ===> %s, for request ===> %s  %s", result.toString(), request.getMethod(), request.getRequestURL()));
        } catch (NullPointerException exception) {
            LOGGER.debug(String.format("REST response ===> %s, (CAN'T GET INFO))", result.toString()));
        }
    }

}
