package edu.virginia.psyc.mindtrails.domain.forms;

import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.domain.recaptcha.RecaptchaForm;
import edu.virginia.psyc.mindtrails.service.ParticipantService;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 9/2/14
 * Time: 3:50 PM
 * For updating a participant.
 */
@Data
public class ParticipantForm implements RecaptchaForm {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantForm.class);

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!#\\$@_'\\+\\?\\[\\]\\.\\- ])(?=.+$).{8,}$";
    public static final String PASSWORD_MESSAGE = "Password must be at least 8 digits long.  It must contain one digit, a lower case letter, an upper case letter, and a special character.";

    @Size(min=2, max=100, message="Please provide a name of at least 3 characters.")
    private String fullName;

    @Email
    @NotNull
    private String email;

    private boolean emailOptout = false;  // User required to receive no more emails.

    private String theme;

    private boolean over18;

    private boolean admin;

    @NotNull
    @Pattern(regexp=PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    private String         password;
    @NotNull
    private String         passwordAgain;

    @NotEmpty(message = "Please complete the Captcha challenge.")
    @NotNull(message = "Please complete the Captcha challenge.")
    private String recaptchaResponse;


    /**
     * Checks to see if the given password matches some standard criteria:
     * @param password
     * @return
     */
    public static boolean validPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }


    public boolean validParticipant(BindingResult bindingResult, ParticipantService service) {

        if(!over18) {
            bindingResult.addError(new ObjectError("over18", "You must be over 18 to participate in this Study."));
        }

        if(service.findByEmail(email) != null) {
            bindingResult.addError(new ObjectError("email", "This email already exists."));
        }

        if(!password.equals(passwordAgain)) {
            bindingResult.addError(new ObjectError("password", "Passwords do not match."));
        }

        if(admin && password.length() < 20) {
            bindingResult.addError(new FieldError("Participant", "admin", "Admin users must have a password of at least 20 characters."));
        }

        if (bindingResult.hasErrors()) {
            LOG.error("Invalid participant:" + bindingResult.getAllErrors());
            return false;
        }
        return true;
    }

    public Participant toParticipant() {
        Participant p = new Participant(fullName, email, admin);
        p.updatePassword(password);
        if(theme != null) p.setTheme(theme);
        p.setOver18(over18);
        p.setLastLoginDate(new Date());
        return(p);
    }


}
