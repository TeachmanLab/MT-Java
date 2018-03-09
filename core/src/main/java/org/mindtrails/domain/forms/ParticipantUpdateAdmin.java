package org.mindtrails.domain.forms;

import lombok.Data;
import org.mindtrails.domain.Participant;

/**
 * For updating a participant through the admin screen
 */
@Data
public class ParticipantUpdateAdmin extends ParticipantUpdate {

    private boolean active;
    private boolean admin;
    private boolean testAccount;

    private boolean blacklist;


    public ParticipantUpdateAdmin() {}

    public ParticipantUpdateAdmin(Participant p) {
        this.fromParticipant(p);
    }

    @Override
    public void fromParticipant(Participant p) {
        super.fromParticipant(p);
        this.active = p.isActive();
        this.admin = p.isAdmin();
        this.testAccount = p.isTestAccount();

        this.blacklist=p.isBlacklist();

    }

    @Override
    public Participant updateParticipant(Participant p) {
        super.updateParticipant(p);
        p.setActive(this.isActive());
        p.setAdmin(this.isAdmin());
        p.setTestAccount(this.isTestAccount());

        p.setBlacklist(this.isBlacklist());

        return p;
    }

}
