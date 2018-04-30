package org.mindtrails.domain.forms;

import lombok.Data;
import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;
import org.mindtrails.domain.recaptcha.RecaptchaForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Allows everything that the ParticipantCreateAdmin form provides
 * but also requires a recaptchaResponse.
 */
@Data
public class ParticipantCreate extends ParticipantCreateAdmin implements RecaptchaForm {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantCreate.class);

    @NotEmpty(message = "Please complete the Captcha challenge.")
    @NotNull(message = "Please complete the Captcha challenge.")
    private String recaptchaResponse;

}
