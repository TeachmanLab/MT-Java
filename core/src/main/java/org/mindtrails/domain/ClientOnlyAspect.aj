package org.mindtrails.domain;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
public aspect ClientOnlyAspect {

    @Value("${mode}")
    private String serverMode;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientOnlyAspect.class);


    @Around("@annotation(ClientOnly)")
    public Object checkServerType(ProceedingJoinPoint joinPoint) throws Throwable {

        LOGGER.info("Server mode dependent method: Checking server mode now:");

        if (serverMode.toLowerCase().equals("client")) {
            Object proceed = joinPoint.proceed();
            return proceed;
        } else {
            return ("/clientOnly");
        }

    }
}
