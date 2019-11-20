package org.mindtrails.domain;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mindtrails.domain.RestExceptions.ExportModeException;
import org.mindtrails.service.ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExportModeAspect {

    private ImportService importService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportModeAspect.class);

    private ExportModeAspect(ImportService service) {
        this.importService = service;
    }

    @Around("@annotation(org.mindtrails.domain.ExportMode)")
    public Object checkServerType(ProceedingJoinPoint joinPoint) throws Throwable {

        if (importService.isExporting()) {
            Object proceed = joinPoint.proceed();
            return proceed;
        } else {
            return false;
        }

    }
}
