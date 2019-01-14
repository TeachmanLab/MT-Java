package edu.virginia.psyc.r34.service;

import edu.virginia.psyc.r34.domain.R34NeutralStudy;
import edu.virginia.psyc.r34.domain.R34Study;
import edu.virginia.psyc.r34.persistence.Questionnaire.DASS21_AS;
import edu.virginia.psyc.r34.persistence.Questionnaire.DASS21_ASRepository;
import org.mindtrails.domain.Conditions.NoNewConditionException;
import org.mindtrails.domain.Conditions.RandomCondition;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.ParticipantServiceImpl;
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

public class R34ParticipantService extends ParticipantServiceImpl implements ParticipantService {

    protected static final Random RANDOM = new Random();  // For generating random CBM and Prime values.

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    DASS21_ASRepository dass21_asRepository;

    List<R34Study.CONDITION> CONDITION_VALUES =
            Collections.unmodifiableList(Arrays.asList(R34Study.CONDITION.values()));
    List<R34Study.PRIME> PRIME_VALUES =
            Collections.unmodifiableList(Arrays.asList(R34Study.PRIME.values()));


    @Override
    public Participant create() {
        Participant p = new Participant();
        R34Study study;
        R34Study.PRIME randomPrime;
        R34Study.CONDITION randomCondition;

        // Set the randome condition and prime.
        randomCondition = CONDITION_VALUES.get(RANDOM.nextInt(CONDITION_VALUES.size()));
        randomPrime     = PRIME_VALUES.get(RANDOM.nextInt(PRIME_VALUES.size()));

        if(randomCondition == R34Study.CONDITION.NEUTRAL) {
            study = new R34NeutralStudy(R34Study.NAME.PRE.toString(), 0, null, new ArrayList<TaskLog>(), this.receiveGiftCards());
        } else {
            study = new R34Study(R34Study.NAME.PRE.toString(), 0, null, new ArrayList<TaskLog>(), this.receiveGiftCards());
        }
        study.setConditioning(randomCondition.name());
        study.setPrime(randomPrime);
        p.setStudy(study);

        return p;
    }

    @Override
    public List<Study> getStudies() {
        List<Study> studies = new ArrayList<>();
        studies.add(new R34Study(R34Study.NAME.PRE.toString(), 0, null, new ArrayList<TaskLog>(), this.receiveGiftCards()));
        studies.add(new R34NeutralStudy(R34Study.NAME.PRE.toString(), 0, null, new ArrayList<TaskLog>(), this.receiveGiftCards()));
        return studies;
    }

    @Override
    public boolean isEligible(HttpSession session) {
        List<DASS21_AS> forms = dass21_asRepository.findBySessionId(session.getId());
        for(DASS21_AS dass21_as: forms) {
            if(dass21_as.eligibleScore()) return true;
        }
        return false;
    }

    @Override
    public void saveNew(Participant p, HttpSession session) {

        save(p);

        // Now that p is saved, connect any DASS21 eligibility data back to the
        // session.
        List<DASS21_AS> forms = dass21_asRepository.findBySessionId(session.getId());
        for(DASS21_AS dass21_as: forms) {
            dass21_as.setParticipant(p);
            dass21_as.setSession("ELIGIBLE");
            dass21_asRepository.save(dass21_as);
        }
    }

    @Override
    public RandomCondition getCondition(Participant p) throws NoNewConditionException {
        return null;
    }

    @Override
    public void markConditionAsUsed(RandomCondition rc) {

    }

    @Override
    public Page<Participant> findEligibleForCoaching(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Participant> searchEligibleForCoaching(Pageable pageable, String searchTerm) {
        return null;
    }


}
