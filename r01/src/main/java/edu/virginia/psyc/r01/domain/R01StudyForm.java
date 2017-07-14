package edu.virginia.psyc.r01.domain;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * For updating the R34 study settings for a participant.
 */
@Data
public class R01StudyForm {

    private R01Study.CONDITION conditioning;
    private String session;
    private int subtractDays = 0;

    public R01StudyForm() {}

    public R01StudyForm(R01Study study)  {

        this.conditioning = study.getConditioning();
        this.session = study.getCurrentSession().getName();
    }

    public void updateStudy(R01Study study) {
        study.setConditioning(this.conditioning);
        study.setCurrentSession(this.session);
        study.setLastSessionDate(DateTime.now().minus(Days.days(subtractDays)).toDate());
    }

}
