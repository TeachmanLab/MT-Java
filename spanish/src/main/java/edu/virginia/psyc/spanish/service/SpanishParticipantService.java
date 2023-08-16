package edu.virginia.psyc.spanish.service;

import edu.virginia.psyc.spanish.domain.SpanishStudy;
import edu.virginia.psyc.spanish.persistence.*;
import org.mindtrails.domain.Conditions.NoNewConditionException;
import org.mindtrails.domain.Conditions.RandomCondition;
import org.mindtrails.domain.Conditions.RandomConditionRepository;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.MissingEligibilityException;
import org.mindtrails.domain.Study;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.ParticipantServiceImpl;
import org.mindtrails.service.TangoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class SpanishParticipantService extends ParticipantServiceImpl implements ParticipantService {

    protected static final String ELIGIBLE_SESSION = "ELIGIBLE";

    String UNASSIGNED_SEGMENT = "unassigned";

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    DASS21_ASRepository dass21RRepository;

    @Autowired
    OARepository oaRepository;

    @Autowired
    DemographicsRepository demographicsRepository;

    @Autowired
    RandomConditionRepository randomBlockRepository;

    @Autowired
    private LocaleResolver localeResolver;


    @Autowired
    TangoService tangoService;

    @Override
    public Participant create() {
        Participant p = new Participant();
        SpanishStudy study = new SpanishStudy();
        p.setReceiveGiftCards(tangoService.getEnabled());
        study.setReceiveGiftCards(tangoService.getEnabled());
        p.setStudy(study);
        return p;
    }

     @Override
     public RandomCondition getCondition(Participant p) throws NoNewConditionException {
        // No segmentation needed on our end for Spanish - will be pre-assigned.
         throw new NoNewConditionException();
     }

    @Override
    public void markConditionAsUsed(RandomCondition rc) {
        // Not used. Conditions are pre-assigned and passed in.
    }


    @Override
    public Page<Participant> findEligibleForCoaching(Pageable pageable) {
        // Returns nothing, as Spanish doesn't do coaching
        List<String> conditions = Arrays.asList("NA");
        return this.participantRepository.findEligibleForCoachingWithOptIn(conditions, pageable);
    }

    @Override
    public Page<Participant> searchEligibleForCoaching(Pageable pageable, String searchTerm) {
        // Returns nothing, as Spanish doesn't do coaching
        List<String> conditions = Arrays.asList("NA");
        return this.participantRepository.searchEligibleForCoachingWithOptIn(conditions, pageable, searchTerm);
    }


    @Override
    public List<Study> getStudies() {
        List<Study> studies = new ArrayList<>();
        studies.add(new SpanishStudy());
        return studies;
    }

    @Override
    public boolean isEligible(HttpSession session) {

        // Safeguard in case someone finds the original account creation path, account/create
        // This will kick them back into the AccountController, which is set to redirect to the eligibilityCheck,
        // which simply produces an error for the Kaiser study.
        if (session.getAttribute("condition") == null) {
            return false;
        }
        DASS21_AS dass = dass21RRepository.findFirstBySessionIdOrderByDateDesc(session.getId());
        OA oa = oaRepository.findFirstBySessionIdOrderByDateDesc(session.getId());
        if (!dass.getOver18().equals("true")) return false;
        if (dass.eligible()) return true;
        return oa.eligible();
    }

    @Override
    public void saveNew(Participant p, HttpSession session) throws MissingEligibilityException {

        Locale locale;
        // Check campaign field to assign condition to participant, 'enb' is the English Bilingual group,
        // 'esb' is the Spanish Bilingual group, 'esd' is the Spanish Dominant group.
        String condition = (String)session.getAttribute("condition");
        p.getStudy().setConditioning(condition);

        p.setEuConsentAgreedDate(p.isEuCitizen() ? Date.from(LocalDateTime.now().atZone(ZoneId.of("America/New_York")).toInstant()) : null);
        //set the campaign that was recorded when the user entered the eligibility controller
        String campaign = (String)session.getAttribute("cp");
        p.setCampaign(campaign);

        // All Spanish Participants should receive gift cards if they are available.
        p.setReceiveGiftCards(tangoService.getEnabled());
        p.getStudy().setReceiveGiftCards(tangoService.getEnabled());

        // Update the participants language based on the condition.
        if(p.getStudy().getConditioning().equals(SpanishStudy.CONDITION.ENGLISH_BILINGUAL.toString())) {
            locale = new Locale("en");
        } else {
            locale = new Locale("es");
        }
        p.setLanguage(locale.toString());

        // Now that p is saved, connect any Expectancy Bias eligibility data back to the
        // session, and log it in the TaskLog.  Update the date time so that it is
        // properly picked up in the export routine.
        List<DASS21_AS> dass_list = dass21RRepository.findBySessionId(session.getId());
        if(dass_list.size() < 1) {
            throw new MissingEligibilityException();
        }

        List<OA> oa_list = oaRepository.findBySessionId(session.getId());
        if(oa_list.size() < 1) {
            throw new MissingEligibilityException();
        }

        save(p);
        SpanishStudy study = (SpanishStudy) p.getStudy();
        for (DASS21_AS e : dass_list) {
            e.setParticipant(p);
            e.setSession(ELIGIBLE_SESSION);
            e.setDate(new Date());
            dass21RRepository.save(e);
            study.completeEligibility(e);
        }
        for (OA oa : oa_list) {
            oa.setParticipant(p);
            oa.setSession(ELIGIBLE_SESSION);
            oa.setDate(new Date());
            oaRepository.save(oa);
            study.completeEligibility(oa);
        }

        save(p); // Just save the participant

    }


}
