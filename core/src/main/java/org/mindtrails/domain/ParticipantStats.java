package org.mindtrails.domain;

import java.util.Date;

/**
 * Created by any on 10/4/17.
 */
public interface ParticipantStats {
    Date getLastLoginDate();
    boolean isActive();
    String getReference();
    //String getCampaign();
    boolean isAdmin();
    boolean isTestAccount();
    long getId();

}
