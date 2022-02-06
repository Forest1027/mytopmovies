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

@Aspect
@Component
public class LoggingHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHandler.class);

    @Before("execution(* com.forest.mytopmovies.controller.*.*.*(..))")
    public void logRequests(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        try {
            HttpServletRequest request = attributes.getRequest();
            LOGGER.debug("REST request ===> {}  {}, in class ===> {} with parameter ===> {}", request.getMethod(),
                    request.getRequestURL(), joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getArgs());
        } catch (NullPointerException exception) {
            LOGGER.debug("REST request (CAN'T GET INFO), in class ===> {} with parameter ===> {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getArgs());
        }

    }

    @AfterReturning(pointcut = "execution(* com.forest.mytopmovies.controller.*.*.*(..))", returning = "result")
    public void logResponse(Object result) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            LOGGER.debug("REST response ===> {}, for request ===> {}  {}", result, request.getMethod(), request.getRequestURL());
        } catch (NullPointerException exception) {
            LOGGER.debug("REST response ===> {}, (CAN'T GET INFO))", result);
        }
    }

}
