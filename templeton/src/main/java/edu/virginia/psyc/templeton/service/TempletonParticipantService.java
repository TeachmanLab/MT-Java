package edu.virginia.psyc.templeton.service;

import edu.virginia.psyc.templeton.domain.TempletonStudy;
import edu.virginia.psyc.templeton.persistence.ExpectancyBias;
import edu.virginia.psyc.templeton.persistence.ExpectancyBiasRepository;
import org.mindtrails.domain.Conditions.NoNewConditionException;
import org.mindtrails.domain.Conditions.RandomCondition;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.MissingEligibilityException;
import org.mindtrails.domain.Study;
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
public class TempletonParticipantService extends ParticipantServiceImpl implements ParticipantService {

    protected static final Random RANDOM = new Random();  // For generating random CBM and Prime values.

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    ExpectancyBiasRepository biasRepository;

    //public enum CONDITION_TYPE  {POSITIVE, POSITIVE_NEGATION, FIFTY_FIFTY_BLOCKED, FIFTY_FIFTY_RANDOM, NEUTRAL}
    public enum CONDITION_SPLIT {A,B}

    List<TempletonStudy.CONDITION> CONDITION_VALUES =
            Collections.unmodifiableList(Arrays.asList(TempletonStudy.CONDITION.values()));

    @Override
    public Participant create() {
        Participant p = new Participant();
        TempletonStudy study = new TempletonStudy();
        p.setStudy(study);
        TempletonStudy.CONDITION type = CONDITION_VALUES.get(RANDOM.nextInt(CONDITION_VALUES.size()));
        study.setConditioning(type.toString());
        return p;
    }

    @Override
    public List<Study> getStudies() {
        List<Study> studies = new ArrayList<>();
        studies.add(new TempletonStudy());
        return studies;
    }


    @Override
    public boolean isEligible(HttpSession session) {
        List<ExpectancyBias> forms = biasRepository.findBySessionId(session.getId());
        for (ExpectancyBias e : forms) {
            if (e.eligible()) return true;
        }
        return false;
    }

    @Override
    public void saveNew(Participant p, HttpSession session) throws MissingEligibilityException {

        List<ExpectancyBias> forms = biasRepository.findBySessionId(session.getId());
        if(forms.size() < 1) {
            throw new MissingEligibilityException();
        }

        save(p);
        TempletonStudy study = (TempletonStudy)p.getStudy();

        // Now that p is saved, connect any Expectancy Bias eligibility data back to the
        // session, and log it in the TaskLog
        for (ExpectancyBias e : forms) {
            e.setParticipant(p);
            e.setSession("ELIGIBLE");
            biasRepository.save(e);
            study.completeEligibility(e);
        }
        save(p); // Re-save participant to record study.
    }

    @Override
    public RandomCondition getCondition(Participant p) throws NoNewConditionException {
        // Templeton assigns conditions immediately, and not part way through the study.
        throw new NoNewConditionException();
    }

    @Override
    public void markConditionAsUsed(RandomCondition rc) {
        // noop.  Condition blocks are not used.
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
