package org.mindtrails.domain.forms;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.forms.validation.HasPhone;
import org.mindtrails.domain.forms.validation.Phone;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The very basic editable things for a participant to change about themselves.
 */
@Data
@Phone(message="Please enter a valid phone number.")
public class ParticipantUpdate implements HasPhone {
    public static final String EMAIL_REGEX = "^(.+)@(\\S+)$";
    public static final String EMAIL_MESSAGE = "Please enter a valid email.";

    @Size(min=2, max=100, message="Please provide a name of at least 3 characters.")
    protected String fullName;

    @Email
    @NotNull
    @Pattern(regexp=EMAIL_REGEX, message = EMAIL_MESSAGE)
    protected String email;

    protected String phone;
    protected String phoneLocale = "US";

    protected boolean emailReminders = true;
    protected boolean phoneReminders = true;
    protected boolean receiveGiftCards = false;
    protected boolean canTextMessage = true;

    protected String timezone;

    protected String theme;

    public void fromParticipant(Participant p) {
        this.email = p.getEmail();
        this.fullName = p.getFullName();
        this.emailReminders = p.isEmailReminders();
        this.phoneReminders = p.isPhoneReminders();
        this.receiveGiftCards = p.isReceiveGiftCards();
        this.theme = p.getTheme();
        this.phone = p.getPhone();
        this.timezone = p.getTimezone();
        this.canTextMessage = p.isCanTextMessage();
    }


    public Participant updateParticipant(Participant p) {
        p.setFullName(this.getFullName());
        p.setEmail(this.getEmail());
        p.setEmailReminders(this.isEmailReminders());
        p.setPhoneReminders(this.isPhoneReminders());
        p.setPhone(formatPhone(this.phone));
        p.setTimezone(this.getTimezone());
        p.setCanTextMessage(this.canTextMessage);
        if(this.theme != null) p.setTheme(this.getTheme());
        return p;
    }


    /**
     * Converts the provided phone number to the E164 standard used by
     * Twilio.
     * @return
     */
    public String formatPhone(String p) {
        if(p == null) return null;

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber phone = phoneUtil.parse(p, phoneLocale);
            return phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            return p; // Leave it alone, let validation handle it.
        }
    }

}
