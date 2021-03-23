package edu.virginia.psyc.kaiser.service;

import edu.virginia.psyc.kaiser.domain.KaiserStudy;
import edu.virginia.psyc.kaiser.persistence.*;
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

import javax.servlet.http.HttpSession;
import java.util.*;

import static edu.virginia.psyc.kaiser.domain.KaiserStudy.CONDITION.CAN_COACH;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class KaiserParticipantService extends ParticipantServiceImpl implements ParticipantService {

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
    AttritionPredictionRepository attritionPredictionRepository;

    @Autowired
    ConditionAssignmentSettingsRepository settingsRepository;

    @Autowired
    TangoService tangoService;

    private ConditionAssignmentSettings settings;
    public final Double defaultThreshold = 0.33d;

    private ConditionAssignmentSettings getSettings() {
        this.settings = this.settingsRepository.findFirstByOrderByLastModifiedDesc();
        if(this.settings == null) {
            this.settings = new ConditionAssignmentSettings();
            this.settings.setAttritionThreshold(this.defaultThreshold); // I don't think this will be used for Kaiser
        }
        return this.settings;
    }

    @Override
    public Participant create() {
        Participant p = new Participant();
        KaiserStudy study = new KaiserStudy();
        study.setStudyExtension(KaiserStudy.STUDY_EXTENSIONS.KAISER.name());
        p.setStudy(study);
        return p;
    }

     @Override
     public RandomCondition getCondition(Participant p) throws NoNewConditionException {
        // No segmentation needed on our end, for Kaiser
         throw new NoNewConditionException();
     }

     // Do we still need this method? - Anna
     @Override
     public void markConditionAsUsed(RandomCondition rc) {
         if(rc.getId() > 0) {  // Some conditions may not actually exist in the database.
             this.randomBlockRepository.delete(rc);
         }
     }

    @Override
    public Page<Participant> findEligibleForCoaching(Pageable pageable) {
        return this.participantRepository.findEligibleForCoachingWithOptIn(CAN_COACH.name(), pageable);
    }

    @Override
    public Page<Participant> searchEligibleForCoaching(Pageable pageable, String searchTerm) {
        return this.participantRepository.searchEligibleForCoachingWithOptIn(CAN_COACH.name(), pageable, searchTerm);
    }

    @Override
    public List<Study> getStudies() {
        List<Study> studies = new ArrayList<>();
        studies.add(new KaiserStudy());
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
        return true; 
    }

    @Override
    public void saveNew(Participant p, HttpSession session) throws MissingEligibilityException {

        Study study = p.getStudy();
        String condition = (String)session.getAttribute("condition");

        // Set the participant's condition based on the session attribute.
        study.setConditioning(condition);

        // Update ability to receive gift cards, based on condition
        if (!condition.contains("BONUS")) {
            p.setReceiveGiftCards(false);
            study.setReceiveGiftCards(false);
        } else {
            p.setReceiveGiftCards(tangoService.getEnabled());
            study.setReceiveGiftCards(tangoService.getEnabled());
        }
        save(p);
        // Generally we would connect any elegibility scores back to the participant at this point, but kaiser does not have this issue.
    }
}