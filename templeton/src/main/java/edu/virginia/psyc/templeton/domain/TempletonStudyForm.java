package edu.virginia.psyc.templeton.domain;

import lombok.Data;

/**
 * For updating the R34 study settings for a participant.
 */
@Data
public class TempletonStudyForm {

    private TempletonStudy.CONDITION conditioning;
    private String session;

    public TempletonStudyForm() {}

    public TempletonStudyForm(TempletonStudy study)  {

        this.conditioning = study.getConditioning();
        this.session = study.getCurrentSession().getName();
    }

    public void updateStudy(TempletonStudy study) {
        study.setConditioning(this.conditioning);
        study.setCurrentSession(this.session);
    }

}
