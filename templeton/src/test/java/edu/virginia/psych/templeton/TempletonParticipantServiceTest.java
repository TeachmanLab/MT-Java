package edu.virginia.psych.templeton;

import edu.virginia.psyc.templeton.domain.TempletonStudy;
import edu.virginia.psyc.templeton.service.TempletonParticipantService;
import org.junit.Test;
import org.mindtrails.domain.Participant;

import static org.junit.Assert.assertTrue;

/**
 * Created by dan on 8/31/16.
 */
public class TempletonParticipantServiceTest {

    @Test
    public void testCreate() {

        TempletonParticipantService service;
        Participant p;
        TempletonStudy study;
        int positive, positive_neg, fifty, fifty_block, neutral;
        positive = 0;
        positive_neg = 0;
        fifty = 0;
        neutral = 0;
        fifty_block =0;

        service = new TempletonParticipantService();

        for(int i=0; i<1000; i++) {
            p = service.create();
            assertTrue(p.getStudy() instanceof TempletonStudy);
            study = (TempletonStudy) p.getStudy();

            switch (study.getConditioning()) {
                case POSITIVE:
                    positive++;
                    break;
                case POSITIVE_NEGATION:
                    positive_neg++;
                    break;
                case FIFTY_FIFTY_RANDOM:
                    fifty++;
                    break;
                case FIFTY_FIFTY_BLOCKED:
                    fifty_block++;
                    break;
                case NEUTRAL:
                    neutral++;
                    break;
            }
        }

        double percent;

        percent = (positive + positive_neg)/1000d;
        assertTrue("Positive is rounghly 30% : " + percent, percent > .22 && percent < .38);
        percent = (fifty + fifty_block)/1000d;
        assertTrue("fiftyfifty is rounghly 30% : " + percent, percent > .22 && percent < .38);
        percent = (neutral)/1000d;
        assertTrue("neutral is rounghly 30% : " + percent, percent > .22 && percent < .38);

        double psPercent = positive / (positive + positive_neg + 0.0d);
        assertTrue("Positive negation is around 1/2 of all positive", psPercent > .42 && psPercent < .58            );

    }
}
