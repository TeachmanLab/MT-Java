package edu.virginia.psyc.kaiser.service;

import edu.virginia.psyc.kaiser.Application;
import edu.virginia.psyc.kaiser.domain.KaiserStudy;
import edu.virginia.psyc.kaiser.persistence.*;
import org.joda.time.DateTime;
import org.joda.time.Period;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class KaiserParticipantServiceTest {

    @Autowired
    KaiserParticipantService service;

    @Autowired
    RandomConditionRepository randomBlockRepository;

    @Autowired
    DemographicsRepository demographicsRepository;

    @Autowired
    AttritionPredictionRepository attritionPredictionRepository;

    @Autowired
    DASS21_ASRepository dassRepository;

    @Autowired
    ConditionAssignmentSettingsRepository settingsRepository;

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
        dass.setSession(KaiserParticipantService.ELIGIBLE_SESSION);
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
    public void testFindEligibleForCoachingWithOptIn() {
        Participant participant = service.create();
        participant.getStudy().setConditioning(KaiserStudy.CONDITION.CAN_COACH.name());
        participant.setWantsCoaching(true);
        participantRepository.saveAndFlush(participant);
        PageRequest pageRequest = new PageRequest(0, 1);
        Page<Participant> eligible = service.findEligibleForCoaching(pageRequest);
        Assert.assertNotNull(eligible);
    }

    // @Test
    // public void testParticipantServiceGeneratesRandomBlocksWhenNeeded() throws NoNewConditionException {

    //     Participant participant = service.create();
    //     Demographics demographics = createDemographics();
    //     demographics.setGender("Male");
    //     demographics.setParticipant(participant);
    //     DASS21_AS dass = createDass();
    //     dass.setParticipant(participant);

    //     participantRepository.saveAndFlush(participant);
    //     demographicsRepository.saveAndFlush(demographics);
    //     dassRepository.saveAndFlush(dass);

    //     String segment = service.getSegmentation(participant);
    //     RandomCondition condition = service.getCondition(participant);

    //     Assert.assertNotNull(condition);
    //     Assert.assertEquals("male_high", segment);
    //     Assert.assertTrue("There should be records in the randomBlockRepository", randomBlockRepository.findAll().size() > 0);
    //     int total = randomBlockRepository.findAll().size();
    //     service.markConditionAsUsed(condition);
    //     Assert.assertTrue("One of the random conditions should now go away", randomBlockRepository.findAll().size() == total - 1);
    // }

    private Participant setupParticipantWithHighRisk() {
        Participant participant = service.create();
        participant.getStudy().setConditioning(KaiserStudy.CONDITION.NO_INCENTIVE.name());
        Demographics demographics = createDemographics();
        demographics.setGender("Male");
        demographics.setParticipant(participant);
        DASS21_AS dass = createDass();
        dass.setParticipant(participant);

        participantRepository.saveAndFlush(participant);
        demographicsRepository.saveAndFlush(demographics);
        dassRepository.saveAndFlush(dass);


        AttritionPrediction pred = new AttritionPrediction();
        pred.setParticipantId(participant.getId());
        pred.setConfidence(0.88d);
        pred.setVersion("1.0");
        pred.setDateCreated(new Date());
        attritionPredictionRepository.save(pred);

        return participant;
    }

    // @Test
    // public void testCoachCondition() throws NoNewConditionException {
    //     Participant participant = setupParticipantWithHighRisk();

    //     // Assure that over the a set of 10 assignments, we get at least one person assigned to the
    //     // coaching condition, and at least one person assigned to the non_coaching condition.
    //     int coaching = 0;
    //     int nonCoaching = 0;
    //     for (int i = 0; i < 20; i++) {
    //         RandomCondition condition = service.getCondition(participant);
    //         if (condition.getValue().equals(KaiserStudy.CONDITION.HR_COACH.name())) coaching++;
    //         if (condition.getValue().equals(KaiserStudy.CONDITION.HR_NO_COACH.name())) nonCoaching++;
    //         service.markConditionAsUsed(condition);
    //         Assert.assertTrue("High Risk participants should be in either coach or nocoach, but was " + condition.getValue(),
    //                 condition.getValue().equals(KaiserStudy.CONDITION.HR_COACH.name()) ||
    //                         condition.getValue().equals(KaiserStudy.CONDITION.HR_NO_COACH.name()));
    //     }
    //     Assert.assertTrue(coaching > 0);
    //     Assert.assertTrue(nonCoaching > 0);
    // }


//    @Test
////    public void itIsPossibleToChangeAttritionThresholdInDatabase() throws Exception {
////        Participant participant = setupParticipantWithHighRisk();
////
////
////        // Create a new setting for Threshold that puts everyone at low risk
////        ConditionAssignmentSettings settings = new ConditionAssignmentSettings();
////        settings.setAttritionThreshold(1d);
////        // put it in the past to avoid race conditions in following code.
////        settings.setLastModified(new DateTime().minus(Period.days(1)).toDate());
////        this.settingsRepository.save(settings);
////
////        // Assure that everyone is marked as high risk.
////        int low = 0;
////        for (int i = 0; i < 20; i++) {
////            RandomCondition condition = service.getCondition(participant);
////            if (condition.getValue().equals(KaiserStudy.CONDITION.LR_TRAINING.name())) low++;
////            service.markConditionAsUsed(condition);
////        }
////        Assert.assertTrue(low == 20);
////
////        // Create a new setting for Threshold that puts everyone at high risk
////        settings = new ConditionAssignmentSettings();
////        settings.setAttritionThreshold(0d);
////        settings.setLastModified(new Date());
////        this.settingsRepository.save(settings);
////
////        // Assure that everyone is marked as low risk.
////        low = 0;
////        for (int i = 0; i < 20; i++) {
////            RandomCondition condition = service.getCondition(participant);
////            if (condition.getValue().equals(KaiserStudy.CONDITION.LR_TRAINING.name())) low++;
////            service.markConditionAsUsed(condition);
////        }
////        Assert.assertTrue("Low should be 0, but was " + low, low == 0);
////
////    }

    /** Using the CSV files and logic from the Segmentation test,
     * build out data as it appears in R01, and show the distribution
     * of how participants are assigned to conditions.
     * @throws Exception
     */
    // @Test
//    @Ignore("This takes a wicked long time to run, but produces a report on the distribution using historical data.")
    // public void segmentationReport() throws Exception {

    //     // Pull in the data
    //     SegmentationTest st = new SegmentationTest();

    //     // Save the records to the database
    //     for(Object id: st.mutualIds) {
    //         Participant p = service.create();
    //         participantRepository.save(p);

    //         DASS21_AS dass = st.dassMap.get(id);
    //         dass.setParticipant(p);
    //         dassRepository.save(dass);

    //         Demographics d = st.demMap.get(id);
    //         d.setParticipant(p);
    //         demographicsRepository.save(d);

    //         AttritionPrediction a = st.attritionMap.get(id);
    //         a.setParticipantId(p.getId());
    //         a.setDateCreated(new Date());
    //         a.setVersion("1.0");
    //         attritionPredictionRepository.save(a);
    //     }
    //     participantRepository.flush();
    //     dassRepository.flush();
    //     demographicsRepository.flush();
    //     attritionPredictionRepository.flush();

    //     // Now assign everybody a condition.
    //     RandomCondition rc;
    //     for(int i = 0; i < 2; i++) { // do this twice, because we need re-assign for people at high risk.
    //         for (Participant p : participantRepository.findAll()) {
    //             try {
    //                 rc = service.getCondition(p);
    //                 p.getStudy().setConditioning(rc.getValue());
    //                 service.markConditionAsUsed(rc);
    //                 service.save(p);
    //             } catch (NoNewConditionException nnce) {
    //             }
    //         }
    //     }
    //     // Now print off the condition counts
    //     printConditionCounts();
    // }

    private void printConditionCounts() {
        Map<String, Integer> counts = new HashMap<>();

        for(Participant p : participantRepository.findAll()) {
            String c = p.getStudy().getConditioning();
            String key = c;
            if(!counts.containsKey(key)) {
                counts.put(key, 1);
            } else {
                counts.put(key, counts.get(key) + 1);
            }
        }

        for (String key : counts.keySet()) {
            System.out.println(key + "," + counts.get(key));
        }
    }



}