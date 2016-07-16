package edu.virginia.psyc.r34;

import edu.virginia.psyc.r34.domain.R34Study;
import edu.virginia.psyc.r34.domain.forms.R34StudyForm;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dan on 7/15/16.
 */
public class R34StudyFormTest {

    @Test
    public void testUpdateStudy() {
        R34StudyForm form = new R34StudyForm();
        R34Study study = new R34Study();

        form.setCondition(R34Study.CONDITION.FIFTY_FIFTY);
        form.setPrime(R34Study.PRIME.ANXIETY);
        form.updateStudy(study);

        assertEquals(R34Study.PRIME.ANXIETY, study.getPrime());
        assertEquals(R34Study.CONDITION.FIFTY_FIFTY, study.getCondition());

    }
}
