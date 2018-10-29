package org.mindtrails.domain.forms;

import lombok.Data;
import org.mindtrails.domain.Participant;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * For updating a participant.
 */
@Data
public class ParticipantCreateAdmin extends ParticipantUpdate {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantCreateAdmin.class);

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!#\\$@_'\\+\\?\\[\\]\\.\\- ])(?=.+$).{8,}$";
    public static final String PASSWORD_MESSAGE = "Password must be at least 8 digits long.  It must contain one digit, a lower case letter, an upper case letter, and a special character.";

    private boolean over18;
    private boolean admin;
    private boolean coach;
    private boolean active = true;
    private boolean testAccount = false;
    private boolean receiveGiftCards = false;
    private boolean verified = false;
    private boolean blacklist = false;

    @NotNull
    @Pattern(regexp=PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    private String         password;
    @NotNull
    private String         passwordAgain;

    /**
     * Checks to see if the given password matches some standard criteria:
     * @param password
     * @return
     */
    public static boolean validPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }


    public boolean validParticipant(BindingResult bindingResult, ParticipantService participantService) {

        if(!over18) {
            bindingResult.rejectValue("over18", "error.over18", "You must be over 18 to participate in this Study.");
        }

        if(participantService.findByEmail(email) != null) {
            bindingResult.rejectValue("email", "error.emailExists", "This email already exists.");
        }
        if(null != phone && !phone.isEmpty() && !participantService.findByPhone(formatPhone(phone)).isEmpty()) {
            bindingResult.rejectValue("phone", "error.phoneExists", "This phone number is already linked to an account.");
        }

        if(!password.equals(passwordAgain)) {
            bindingResult.rejectValue("password", "error.passwordMatch", "Passwords do not match.");
        }

        if(admin && password.length() < 20) {
            bindingResult.rejectValue("password", "error.passwordAdmin", "Admin users must have a password of at least 20 characters.");
        }

        if (bindingResult.hasErrors()) {
            LOG.error("Invalid participant:" + bindingResult.getAllErrors());
            return false;
        }
        return true;
    }

    @Override
    public void fromParticipant(Participant p) {
        super.fromParticipant(p);
        this.setOver18(p.isOver18());
        this.setActive(p.isActive());
        this.setAdmin(p.isAdmin());
        this.setCoach(p.isCoach());
        this.setTestAccount(p.isTestAccount());
    }

    @Override
    public Participant updateParticipant(Participant p) {
        super.updateParticipant(p);
        p.updatePassword(password);
        p.setOver18(over18);
        p.setActive(active);
        p.setAdmin(admin);
        p.setCoach(coach);
        p.setTestAccount(testAccount);
        return(p);
    }


}
