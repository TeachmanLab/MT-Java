package edu.virginia.psyc.r34.domain.forms;

import edu.virginia.psyc.r34.domain.R34Study;
import lombok.Data;

/**
 * For updating the R34 study settings for a participant.
 */
@Data
public class R34StudyForm {

    private String condition;
    private R34Study.PRIME prime;

    public R34StudyForm() {}

    public R34StudyForm(R34Study study)  {
        this.condition = study.getConditioning();
        this.prime = study.getPrime();
    }

    public void updateStudy(R34Study study) {
        study.setConditioning(this.condition);
        study.setPrime(this.prime);
    }

}
