package edu.virginia.psyc.r01.service;

import edu.virginia.psyc.r01.Application;
import edu.virginia.psyc.r01.domain.R01Study;
import edu.virginia.psyc.r01.persistence.DASS21_AS;
import edu.virginia.psyc.r01.persistence.DASS21_ASRepository;
import edu.virginia.psyc.r01.persistence.Demographics;
import edu.virginia.psyc.r01.persistence.DemographicsRepository;
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
        dass.setSessionId(R01Study.PRE_TEST);
        dass.setDryness(2);
        dass.setBreathing(2);
        dass.setTrembling(2);
        dass.setWorry(2);
        dass.setPanic(2);
        dass.setHeart(2);
        dass.setScared(2);
        dass.setDate(new Date());
        return(dass);
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


}
