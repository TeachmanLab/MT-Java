package org.mindtrails.domain;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mindtrails.domain.RestExceptions.ImportModeException;
import org.mindtrails.service.ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ImportModeAspect {

    private ImportService importService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportModeAspect.class);

    private ImportModeAspect(ImportService service) {
        this.importService = service;
    }


    @Around("@annotation(ImportMode)")
    public Object checkServerType(ProceedingJoinPoint joinPoint) throws Throwable {

        if (importService.isImporting()) {
            Object proceed = joinPoint.proceed();
            return proceed;
        } else {
            throw new ImportModeException();
        }

    }
}