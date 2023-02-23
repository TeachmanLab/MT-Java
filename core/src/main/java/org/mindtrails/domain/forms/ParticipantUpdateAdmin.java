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

    private boolean export; //added for export role
    private boolean coaching;
    private boolean testAccount;
    private boolean blacklist;
    private Participant coachedBy;

    public ParticipantUpdateAdmin() {}

    public ParticipantUpdateAdmin(Participant p) {
        this.fromParticipant(p);
    }

    @Override
    public void fromParticipant(Participant p) {
        super.fromParticipant(p);
        this.active = p.isActive();
        this.admin = p.isAdmin();
        this.export=p.isExport();
        this.coaching = p.isCoaching();
        this.testAccount = p.isTestAccount();
        this.blacklist=p.isBlacklist();
        this.coachedBy = p.getCoachedBy();
    }

    @Override
    public Participant updateParticipant(Participant p) {
        super.updateParticipant(p);
        p.setActive(this.isActive());
        p.setAdmin(this.isAdmin());
        p.setExport(this.isExport());
        p.setCoaching(this.isCoaching());
        p.setTestAccount(this.isTestAccount());
        p.setBlacklist(this.isBlacklist());
        p.setCoachedBy(this.coachedBy);
        return p;
    }

}
