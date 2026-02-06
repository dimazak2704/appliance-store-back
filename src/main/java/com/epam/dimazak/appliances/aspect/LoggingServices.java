package com.epam.dimazak.appliances.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingServices {

    @Around("@annotation(com.epam.dimazak.appliances.aspect.Loggable)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("--> START method '{}' with arguments: {}", methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            log.error("<-- ERROR in method '{}': {}", methodName, ex.getMessage());
            throw ex;
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        log.info("<-- END method '{}'. Return: {}. Time taken: {} ms", methodName, result, timeTaken);

        return result;
    }
}