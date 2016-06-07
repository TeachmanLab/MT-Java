package edu.virginia.psyc.pi.domain;

import edu.virginia.psyc.pi.domain.recaptcha.RecaptchaForm;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 9/2/14
 * Time: 3:50 PM
 * For updating a participant.
 */
@Data
public class ParticipantCreateForm implements RecaptchaForm {

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!#\\$@_'\\+\\?\\[\\]\\.\\- ])(?=.+$).{8,}$";
    public static final String PASSWORD_MESSAGE = "Password must be at least 8 digits long.  It must contain one digit, a lower case letter, an upper case letter, and a special character.";

    @Size(min=2, max=100, message="Please provide a name of at least 3 characters.")
    private String fullName;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Pattern(regexp=PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    private String         password;

    @NotNull
    private String         passwordAgain;

    private boolean over18 = false;

    private boolean admin = false;

    @NotEmpty(message = "Please complete the Captcha challenge.")
    @NotNull(message = "Please complete the Captcha challenge.")
    private String recaptchaResponse;


    public Participant toParticipant() {
        Participant p = new Participant();
        p.setFullName(this.fullName);
        p.setEmail(this.email);
        p.setPassword(this.password);
        p.setOver18(this.over18);
        p.setAdmin(this.admin);
        return p;
    }


    /**
     * Checks to see if the given password matches some standard criteria:
     *
     * @param password
     * @return
     */
    public static boolean validPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

}
