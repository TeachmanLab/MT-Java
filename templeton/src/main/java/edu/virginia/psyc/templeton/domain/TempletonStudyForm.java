package edu.virginia.psyc.templeton.domain;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * For updating the R34 study settings for a participant.
 */
@Data
public class TempletonStudyForm {

    private String conditioning;
    private String session;
    private int subtractDays = 0;

    public TempletonStudyForm() {}

    public TempletonStudyForm(TempletonStudy study)  {

        this.conditioning = study.getConditioning().toString();
        this.session = study.getCurrentSession().getName();
    }

    public void updateStudy(TempletonStudy study) {
        study.setConditioning(this.conditioning);
        study.setCurrentSession(this.session);
        study.setLastSessionDate(DateTime.now().minus(Days.days(subtractDays)).toDate());
    }

}
