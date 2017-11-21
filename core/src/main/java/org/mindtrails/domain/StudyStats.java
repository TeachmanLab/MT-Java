package org.mindtrails.domain;

import java.util.Date;

/**
 * Created by any on 10/4/17.
 */
public interface StudyStats {
    String getCurrentSession();
    int getCurrentTaskIndex();
    Date getLastSessionDate();
    String getConditioning();
    long getId();

}
