package org.mindtrails.domain.recaptcha;

import org.mindtrails.domain.ClientOnly;
import org.mindtrails.domain.RestExceptions.RecaptchaServiceException;
import org.mindtrails.service.RecaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * The scope here is critical.  It is Request Scoped, being created anew for
 * each request - allowing the httpservicerequest to be injected so we can
 * calculate the users remote address; which we need to validate the Recaptcha
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RecaptchaFormValidator implements Validator {

    private static final String ERROR_RECAPTCHA_INVALID = "recaptcha.error.invalid";
    private static final String ERROR_RECAPTCHA_UNAVAILABLE = "recaptcha.error.unavailable";
    private final HttpServletRequest httpServletRequest;
    private final RecaptchaService recaptchaService;


    @Autowired
    public RecaptchaFormValidator(HttpServletRequest httpServletRequest, RecaptchaService recaptchaService) {
        this.httpServletRequest = httpServletRequest;
        this.recaptchaService = recaptchaService;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return RecaptchaForm.class.isAssignableFrom(clazz);
    }

    /**
     * Will attempt the get the id from the request headers - useful if the
     * webserver sits behind a proxy (as is the case for mindtrails.org)
     * @param request
     * @return
     */
    private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public void validate(Object target, Errors errors) {
        RecaptchaForm form = (RecaptchaForm) target;
        try {
            if (form.getRecaptchaResponse() != null
                    && !form.getRecaptchaResponse().isEmpty()
                    && !recaptchaService.isResponseValid(getRemoteIp(httpServletRequest), form.getRecaptchaResponse())) {
                errors.reject(ERROR_RECAPTCHA_INVALID);
                errors.rejectValue("recaptchaResponse", ERROR_RECAPTCHA_INVALID);
            }
        } catch (RecaptchaServiceException e) {
            errors.reject(ERROR_RECAPTCHA_UNAVAILABLE);
        }
    }
}