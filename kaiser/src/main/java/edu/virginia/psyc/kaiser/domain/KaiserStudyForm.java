package edu.virginia.psyc.kaiser.domain;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * For updating the Kaiser study settings for a participant.
 */
@Data
public class KaiserStudyForm {

    private String conditioning;
    private String session;
    private int subtractDays = 0;

    public KaiserStudyForm() {}

    public KaiserStudyForm(KaiserStudy study)  {

        this.conditioning = study.getConditioning();
        this.session = study.getCurrentSession().getName();
    }

    public void updateStudy(KaiserStudy study) {
        study.setConditioning(this.conditioning);
        study.setCurrentSession(this.session);
        study.setLastSessionDate(DateTime.now().minus(Days.days(subtractDays)).toDate());
    }

}
