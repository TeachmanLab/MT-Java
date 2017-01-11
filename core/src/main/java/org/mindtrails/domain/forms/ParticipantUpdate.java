package org.mindtrails.domain.forms;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.mindtrails.domain.Participant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The very basic editable things for a participant to change about themselves.
 */
@Data
public class ParticipantUpdate {

    @Size(min=2, max=100, message="Please provide a name of at least 3 characters.")
    protected String fullName;

    @Email
    @NotNull
    protected String email;

    protected boolean emailOptout = false;  // User required to receive no more emails.

    protected String theme;

    public void fromParticipant(Participant p) {
        this.email = p.getEmail();
        this.fullName = p.getFullName();
        this.emailOptout = p.isEmailOptout();
        this.theme = p.getTheme();
    }

    public Participant toParticipant() {
        Participant p = new Participant();
        return updateParticipant(p);
    }

    public Participant updateParticipant(Participant p) {
        p.setFullName(this.getFullName());
        p.setEmail(this.getEmail());
        p.setEmailOptout(this.isEmailOptout());
        if(this.theme != null) p.setTheme(this.getTheme());
        return p;
    }
}
