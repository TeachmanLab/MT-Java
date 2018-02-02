package edu.virginia.psyc.hobby.domain;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * For updating the R34 study settings for a participant.
 */
@Data
public class HobbyStudyForm {

    private String conditioning;
    private String session;
    private int subtractDays = 0;

    public HobbyStudyForm() {}

    public HobbyStudyForm(HobbyStudy study)  {

        this.conditioning = study.getConditioning();
        this.session = study.getCurrentSession().getName();
    }

    public void updateStudy(HobbyStudy study) {
        study.setConditioning(this.conditioning);
        study.setCurrentSession(this.session);
        study.setLastSessionDate(DateTime.now().minus(Days.days(subtractDays)).toDate());
    }

}
