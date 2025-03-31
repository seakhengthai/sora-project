package com.demo.payment.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class LogAroundAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAroundAspect.class);

    @Before("@annotation(com.demo.payment.logger.LogAround)")
    public void writeLogBefore(JoinPoint joinPoint) throws NoSuchMethodException {
        LOGGER.info("===== Start: {} =====", this.getMessage(joinPoint));
    }

    @AfterReturning("@annotation(com.demo.payment.logger.LogAround)")
    public void writeLogAfterReturn(JoinPoint joinPoint) throws NoSuchMethodException {
        LOGGER.info("===== End with success: {} =====", this.getMessage(joinPoint));
    }

    @AfterThrowing(value = "@annotation(com.demo.payment.logger.LogAround)", throwing = "e")
    public void writeLogAfterThrow(JoinPoint joinPoint, Exception e) throws NoSuchMethodException {
        LOGGER.error("There is an error occur in process", e);
        LOGGER.error("===== End with failure: {} =====", this.getMessage(joinPoint));
    }

    private String getMessage(JoinPoint joinPoint) throws NoSuchMethodException {
        Method interfaceMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Method implementationMethod = joinPoint.getTarget().getClass()
                .getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());

        String message = null;
        if (implementationMethod.isAnnotationPresent(LogAround.class)) {
            LogAround logAround = implementationMethod.getAnnotation(LogAround.class);
            message = logAround.message();
        }

        return message;
    }
}
