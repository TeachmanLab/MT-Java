package edu.virginia.psyc.r01.service;

import edu.virginia.psyc.r01.Application;
import edu.virginia.psyc.r01.domain.R01Study;
import edu.virginia.psyc.r01.persistence.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.domain.Conditions.NoNewConditionException;
import org.mindtrails.domain.Conditions.RandomCondition;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.domain.Conditions.RandomConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class R01ParticipantServiceTest {

    @Autowired
    R01ParticipantService service;

    @Autowired
    RandomConditionRepository randomBlockRepository;

    @Autowired
    DemographicsRepository demographicsRepository;

    @Autowired
    AttritionPredictionRepository attritionPredictionRepository;


    @Autowired
    DASS21_ASRepository dassRepository;


    @Autowired
    ParticipantRepository participantRepository;


    private Demographics createDemographics() {
        Demographics d = new Demographics();
        d.setTimeOnPage(1000);
        d.setDate(new Date());
        d.setBirthYear(1996);
        d.setEthnicity("Cracker");
        d.setCountry("Constantinople");
        d.setEducation("Hard Knocks");
        d.setMaritalStat("bachelor");
        d.setIncome("mostly poor");
        d.setEmploymentStat("gainfully bumming");
        d.setPtpReason("unsure");
        d.setPtpReasonOther("unset");
        d.setGender("male");
        d.setRace(new ArrayList<>());
        return d;
    }

    private DASS21_AS createDass() {
        DASS21_AS dass = new DASS21_AS();
        dass.setOver18("true");
        dass.setSession(R01ParticipantService.ELIGIBLE_SESSION);
        dass.setDryness(2);
        dass.setBreathing(2);
        dass.setTrembling(2);
        dass.setWorry(2);
        dass.setPanic(2);
        dass.setHeart(2);
        dass.setScared(2);
        dass.setDate(new Date());
        return (dass);
    }


    @Test
    public void testParticipantServiceGeneratesRandomBlocksWhenNeeded() throws NoNewConditionException {

        Participant participant = service.create();
        Demographics demographics = createDemographics();
        demographics.setGender("Male");
        demographics.setParticipant(participant);
        DASS21_AS dass = createDass();
        dass.setParticipant(participant);

        participantRepository.saveAndFlush(participant);
        demographicsRepository.saveAndFlush(demographics);
        dassRepository.saveAndFlush(dass);

        String segment = service.getSegmentation(participant);
        RandomCondition condition = service.getCondition(participant);

        Assert.assertNotNull(condition);
        Assert.assertEquals("male_high", segment);
        Assert.assertTrue("There should be records in the randomBlockRepository", randomBlockRepository.findAll().size() > 0);
        Assert.assertTrue("This should have created 49 blocks", randomBlockRepository.findAll().size() == 49);
        service.markConditionAsUsed(condition);
        Assert.assertTrue("One of the random conditions should now go away", randomBlockRepository.findAll().size() == 48);
    }


    @Test
    public void testCoachCondition() throws NoNewConditionException {

        Participant participant = service.create();
        participant.getStudy().setConditioning(R01Study.CONDITION.TRAINING.name());
        Demographics demographics = createDemographics();
        demographics.setGender("Male");
        demographics.setParticipant(participant);
        DASS21_AS dass = createDass();
        dass.setParticipant(participant);

        participantRepository.saveAndFlush(participant);
        demographicsRepository.saveAndFlush(demographics);
        dassRepository.saveAndFlush(dass);


        AttritionPrediction pred = new AttritionPrediction(participant, 88d, 90d);
        attritionPredictionRepository.save(pred);

        // Assure that over the a set of 10 assignments, we get at least one person assigned to the
        // coaching condition, and at least one person assigned to the non_coaching condition.
        int coaching = 0;
        int nonCoaching = 0;
        for (int i = 0; i < 20; i++) {
            RandomCondition condition = service.getCondition(participant);
            if (condition.getValue().equals(R01Study.CONDITION.HR_COACH.name())) coaching++;
            if (condition.getValue().equals(R01Study.CONDITION.HR_NOCOACH.name())) nonCoaching++;
            service.markConditionAsUsed(condition);
            Assert.assertTrue("High Risk participants should be in either coach or nocoach",
                    condition.getValue().equals(R01Study.CONDITION.HR_COACH.name()) ||
                            condition.getValue().equals(R01Study.CONDITION.HR_NOCOACH.name()));
        }
        Assert.assertTrue(coaching > 0);
        Assert.assertTrue(nonCoaching > 0);
    }

    /** Using the CSV files and logic from the Segmentation test,
     * build out data as it appears in R01, and show the distribution
     * of how participants are assigned to conditions.
     * @throws Exception
     */
    @Test
    public void segmentationReport() throws Exception {

        // Pull in the data
        SegmentationTest st = new SegmentationTest();

        // Save the records to the database
        for(Object id: st.mutualIds) {
            Participant p = service.create();
            participantRepository.save(p);

            DASS21_AS dass = st.dassMap.get(id);
            dass.setParticipant(p);
            dassRepository.save(dass);

            Demographics d = st.demMap.get(id);
            d.setParticipant(p);
            demographicsRepository.save(d);

            AttritionPrediction a = st.attritionMap.get(id);
            a.setParticipant(p);
            attritionPredictionRepository.save(a);
        }




    }


}