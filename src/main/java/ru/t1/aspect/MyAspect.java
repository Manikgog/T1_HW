package ru.t1.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.List;

@Aspect
@Component
@Order(1)
public class MyAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAspect.class);

    @Before("@annotation(ru.t1.annotation.LogExecution)")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Calling method -> " +joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "@annotation(ru.t1.annotation.LogException)")
    public void logAfterThrowing(JoinPoint joinPoint) {
        LOGGER.info("Exception throwing in method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "@annotation(ru.t1.annotation.ResultHandling)", returning = "list")
    public void afterReturning(JoinPoint joinPoint, List<Integer> list) {
        LOGGER.info("Result handling in method: " + joinPoint.getSignature().getName());
        if(list.isEmpty()){
            LOGGER.info("List<Integer> is empty");
        }else{
            LOGGER.info("List<Integer> is " + list);
        }
    }

    @Around("@annotation(ru.t1.annotation.ExecutionTime)")
    public Object around(ProceedingJoinPoint joinPoint) {
        LOGGER.info("Advice \"around\". Calling method -> " + joinPoint.getSignature().getName());
        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("Advice \"around\". Execution time: " + (endTime - startTime) + " ms");
        return result;
    }

}

