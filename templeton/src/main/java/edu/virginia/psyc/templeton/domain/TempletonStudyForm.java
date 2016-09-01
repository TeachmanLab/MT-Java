package edu.virginia.psyc.templeton.domain;

import lombok.Data;

/**
 * For updating the R34 study settings for a participant.
 */
@Data
public class TempletonStudyForm {

    private TempletonStudy.CONDITION conditioning;

    public TempletonStudyForm() {}

    public TempletonStudyForm(TempletonStudy study)  {
        this.conditioning = study.getConditioning();
    }

    public void updateStudy(TempletonStudy study) {
        study.setConditioning(this.conditioning);
    }

}
