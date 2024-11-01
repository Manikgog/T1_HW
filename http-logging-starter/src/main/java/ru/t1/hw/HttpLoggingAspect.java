package ru.t1.hw;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import ru.t1.hw.config.HttpLoggingProperties;
import java.util.Collections;
import java.util.stream.Collectors;

@Aspect
@Component
public class HttpLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(HttpLoggingAspect.class);

    private final HttpLoggingProperties properties;

    public HttpLoggingAspect(HttpLoggingProperties properties) {
        this.properties = properties;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {}

    @Before("restController()")
    public void logRequest(JoinPoint joinPoint) {
        if (!properties.getEnabled()) return;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        if (properties.getLevel().equals("DEBUG")) {
            log.debug("{}.{}, args -> {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                    joinPoint.getArgs());
            log.debug("Headers: {}", Collections.list(request.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(h -> h, request::getHeader)));
        }else{
            log.info("{}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            log.info("HTTP {} request to {}", request.getMethod(), request.getRequestURI());
        }
    }

    @AfterReturning(pointcut = "restController()", returning = "response")
    public void logResponse(Object response) {
        if (properties.getEnabled()) return;
        if (properties.getLevel().equals("DEBUG")) {
            log.debug("HTTP response: {}", response);
            return;
        }
        log.info("HTTP response: {}", response);
    }
}
