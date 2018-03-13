package org.mindtrails.domain;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mindtrails.domain.RestExceptions.DataOnlyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataOnlyAspect {

    @Value("${mode}")
    private String serverMode;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataOnlyAspect.class);


    @Around("@annotation(DataOnly)")
    public Object checkServerType(ProceedingJoinPoint joinPoint) throws Throwable {

        LOGGER.info("Server mode dependent method: Checking server mode now:");

        if (serverMode.toLowerCase().equals("data")) {
            Object proceed = joinPoint.proceed();
            return proceed;
        } else {
            LOGGER.info("Not in the data mode. This function is disabled. Hint: you are at "+serverMode+".");
            throw new DataOnlyException();
        }

    }
}