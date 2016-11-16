package org.mindtrails.domain.forms;

import lombok.Data;
import org.mindtrails.domain.Participant;

/**
 * For updating a participant through the admin screen
 */
@Data
public class ParticipantUpdateAdmin extends ParticipantUpdate {

    private boolean active;  // User required to receive no more emails.
    private boolean admin;  // User required to receive no more emails.
    private boolean isTest;
    public ParticipantUpdateAdmin() {}

    public ParticipantUpdateAdmin(Participant p) {
        this.fromParticipant(p);
    }

    @Override
    public void fromParticipant(Participant p) {
        super.fromParticipant(p);
        this.active = p.isActive();
        this.admin = p.isAdmin();
        this.isTest = p.isTest();
    }

    @Override
    public Participant updateParticipant(Participant p) {
        super.updateParticipant(p);
        p.setActive(this.isActive());
        p.setAdmin(this.isAdmin());
        p.setTest(this.isTest());
        return p;
    }

}
