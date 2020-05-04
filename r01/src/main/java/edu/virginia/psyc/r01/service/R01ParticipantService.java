package edu.virginia.psyc.r01.service;

import edu.virginia.psyc.r01.domain.R01Study;
import edu.virginia.psyc.r01.persistence.*;
import org.mindtrails.domain.Conditions.ConditionAssignment;
import org.mindtrails.domain.Conditions.NoNewConditionException;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Conditions.RandomCondition;
import org.mindtrails.domain.RestExceptions.MissingEligibilityException;
import org.mindtrails.domain.Study;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.domain.Conditions.RandomConditionRepository;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.ParticipantServiceImpl;
import org.mindtrails.service.TangoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class R01ParticipantService extends ParticipantServiceImpl implements ParticipantService {

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
            this.settings.setAttritionThreshold(this.defaultThreshold);
        }
        return this.settings;
    }

    @Override
    public Participant create() {
        Participant p = new Participant();
        R01Study study = new R01Study();
        study.setStudyExtension("TET");  // All new studies are TET Studies,an extension of R01.
        p.setReceiveGiftCards(tangoService.getEnabled());
        study.setReceiveGiftCards(tangoService.getEnabled());
        p.setStudy(study);
        study.setConditioning(R01Study.CONDITION.NONE.name());
        return p;
    }

    @Override
    public RandomCondition getCondition(Participant p) throws NoNewConditionException {
        String segmentation = getSegmentation(p);

        // We have to know their segmentation to set a new condition.
        if (segmentation.equals(UNASSIGNED_SEGMENT)) throw new NoNewConditionException();

        // For NONE, and TRAINING, it may be possible to update the condition.
        RandomCondition assignment;
        R01Study.CONDITION condition = R01Study.CONDITION.valueOf(p.getStudy().getConditioning());
        switch (condition) {
            case NONE:
                assignment = nextAssignmentForSegment(segmentation);
                break;
            case TRAINING:
                AttritionPrediction pred = attritionPredictionRepository.findOne(p.getId());
                if (pred != null) {
                    if(pred.getConfidence() > this.getSettings().getAttritionThreshold()) {
                        assignment = nextAssignmentForCoaching(segmentation);
                    } else {
                        assignment = new RandomCondition(R01Study.CONDITION.LR_TRAINING.toString(), "na");
                    }
                } else {
                    // We don't have a prediction for this participant yet.
                    throw new NoNewConditionException();
                }
                break;
            default:  // once assigned to control, coach or no_coach, we no longer assign new conditions.
                throw new NoNewConditionException();
        }
        return assignment;
    }

    @Override
    public void markConditionAsUsed(RandomCondition rc) {
        if(rc.getId() > 0) {  // Some conditions may not actually exist in the database.
            this.randomBlockRepository.delete(rc);
        }
    }

    @Override
    public Page<Participant> findEligibleForCoaching(Pageable pageable) {
        return this.participantRepository.findEligibleForCoaching(R01Study.CONDITION.HR_COACH.name(), pageable);
    }

    @Override
    public Page<Participant> searchEligibleForCoaching(Pageable pageable, String searchTerm) {
        return this.participantRepository.searchEligibleForCoaching(R01Study.CONDITION.HR_COACH.name(), pageable, searchTerm);
    }

    protected String getSegmentation(Participant p) {
        Demographics demographics = demographicsRepository.findFirstByParticipantId(p.getId());
        DASS21_AS dass = dass21RRepository.findFirstByParticipantAndSession(p, ELIGIBLE_SESSION);
        if(demographics != null && dass != null) {
            return getSegmentFromDassAndDemographics(dass, demographics);
        } else {
            return UNASSIGNED_SEGMENT;
        }
    }

    protected String getSegmentFromDassAndDemographics(DASS21_AS dass, Demographics demographics) {
        String gender_seg = demographics.calculateSegmentation();
        String dass_seg = dass.calculateSegmentation();
        return gender_seg + "_" + dass_seg;
    }


    private RandomCondition nextAssignmentForSegment(String segment) {
        assureRandomAssignmentsAvailableForSegment(segment);
        RandomCondition assignment = randomBlockRepository.findFirstBySegmentNameOrderById(segment);
        return assignment;
    }

    private RandomCondition nextAssignmentForCoaching(String segment) {
        String coachSegment = "coach_" + segment;
        assureRandomAssignmentsAvailableForCoaching(coachSegment);
        RandomCondition assignment = randomBlockRepository.findFirstBySegmentNameOrderById(coachSegment);
        return assignment;
    }

    /**
     * Random blocks for initial assignment are split 75/25 for training and control respectively.
     * NOTE: 10/30/2019 - modified to be a 90/10 split between training and control to
     * account for an inbalance in assignments that occurred due to early attrition.
     * @param segment
     */
    private void original_assureRandomAssignmentsAvailableForSegment(String segment) {
        if(randomBlockRepository.countAllBySegmentName(segment) < 1) {
            Map<String, Float> valuePercentages = new HashMap<>();
            valuePercentages.put(R01Study.CONDITION.TRAINING.name(), 90.0f);
            valuePercentages.put(R01Study.CONDITION.CONTROL.name(), 10.0f);
            List<RandomCondition> blocks = RandomCondition.createBlocks(valuePercentages, 50, segment);
            this.randomBlockRepository.save(blocks);
            this.randomBlockRepository.flush();
        }
    }

    /**
     * This is the second study, which divided users up into 4 different conditions
     * all of which were training conditions.
     * @param segment
     */
    private void assureRandomAssignmentsAvailableForSegment(String segment) {
        if(randomBlockRepository.countAllBySegmentName(segment) < 1) {
            Map<String, Float> valuePercentages = new HashMap<>();
            valuePercentages.put(R01Study.CONDITION.TRAINING_ORIG.name(), 25.0f);
            valuePercentages.put(R01Study.CONDITION.TRAINING_30.name(), 25.0f);
            valuePercentages.put(R01Study.CONDITION.TRAINING_ED.name(), 25.0f);
            valuePercentages.put(R01Study.CONDITION.TRAINING_CREATE.name(), 25.0f);
            List<RandomCondition> blocks = RandomCondition.createBlocks(valuePercentages, 50, segment);
            this.randomBlockRepository.save(blocks);
            this.randomBlockRepository.flush();
        }
    }



    /**
     * Random blocks for coaching assignment are split 50/50 for coaching and no coaching
     * @param segment
     */
    private void assureRandomAssignmentsAvailableForCoaching(String segment) {
        if(randomBlockRepository.countAllBySegmentName(segment) < 1) {
            Map<String, Float> valuePercentages = new HashMap<>();
            valuePercentages.put(R01Study.CONDITION.HR_COACH.name(), 50.0f);
            valuePercentages.put(R01Study.CONDITION.HR_NO_COACH.name(), 50.0f);
            List<RandomCondition> blocks = RandomCondition.createBlocks(valuePercentages, 50, segment);
            this.randomBlockRepository.save(blocks);
            this.randomBlockRepository.flush();
        }
    }

    @Override
    public List<Study> getStudies() {
        List<Study> studies = new ArrayList<>();
        studies.add(new R01Study());
        return studies;
    }

    @Override
    public boolean isEligible(HttpSession session) {
        DASS21_AS dass = dass21RRepository.findFirstBySessionIdOrderByDateDesc(session.getId());
        OA oa = oaRepository.findFirstBySessionIdOrderByDateDesc(session.getId());
        if (!dass.getOver18().equals("true")) return false;
        if (dass.eligible()) return true;
        return oa.eligible();
    }

    @Override
    public void saveNew(Participant p, HttpSession session) throws MissingEligibilityException {

        List<DASS21_AS> dass_list = dass21RRepository.findBySessionId(session.getId());
        if(dass_list.size() < 1) {
            throw new MissingEligibilityException();
        }

        List<OA> oa_list = oaRepository.findBySessionId(session.getId());
        if(oa_list.size() < 1) {
            throw new MissingEligibilityException();
        }

        save(p);
        R01Study study = (R01Study)p.getStudy();

        // Now that p is saved, connect any Expectancy Bias eligibility data back to the
        // session, and log it in the TaskLog.  Update the date time so that it is
        // properly picked up in the export routine.
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


        save(p); // Re-save participant to record study.
    }
}