package edu.virginia.psyc.r34.MockClasses;

import org.mindtrails.service.RecaptchaService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Handles Recaptcha - a means for identifying the difference between a
 * person submitting a form, and a script or program submitting that form.
 * Taken verbatim from:
 * http://kielczewski.eu/2015/07/spring-recaptcha-v2-form-validation/
 */

@Service
@Primary
public class MockRecaptchaService implements RecaptchaService {

    @Override
    public boolean isResponseValid(String remoteIp, String response) {
        return true;
    }

}
