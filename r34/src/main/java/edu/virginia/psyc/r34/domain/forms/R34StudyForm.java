package edu.virginia.psyc.r34.domain.forms;

import edu.virginia.psyc.r34.domain.R34Study;
import lombok.Data;

/**
 * For updating the R34 study settings for a participant.
 */
@Data
public class R34StudyForm {

    private R34Study.CONDITION condition;
    private R34Study.PRIME prime;

    public R34StudyForm() {}

    public R34StudyForm(R34Study study)  {
        this.condition = study.getCondition();
        this.prime = study.getPrime();
    }

    public void updateStudy(R34Study study) {
        study.setCondition(this.condition);
        study.setPrime(this.prime);
    }

}
