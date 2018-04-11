package edu.virginia.psyc.r01.service;

import edu.virginia.psyc.r01.domain.R01Study;
import edu.virginia.psyc.r01.persistence.ExpectancyBias;
import edu.virginia.psyc.r01.persistence.ExpectancyBiasRepository;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.MissingEligibilityException;
import org.mindtrails.domain.Study;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.ParticipantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class R01ParticipantService extends ParticipantServiceImpl implements ParticipantService {

    protected static final Random RANDOM = new Random();  // For generating random CBM and Prime values.

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    ExpectancyBiasRepository biasRepository;

    public enum CONDITION_TYPE  {POSITIVE, FIFTY_FIFTY, NEUTRAL}
    public enum CONDITION_SPLIT {A,B}

    List<CONDITION_TYPE> CONDITION_VALUES =
            Collections.unmodifiableList(Arrays.asList(CONDITION_TYPE.values()));

    @Override
    public Participant create() {
        Participant p = new Participant();
        R01Study study = new R01Study();
        p.setStudy(study);
        p.setReceiveGiftCards(true);

        CONDITION_TYPE type = CONDITION_VALUES.get(RANDOM.nextInt(CONDITION_VALUES.size()));
        if(type == CONDITION_TYPE.POSITIVE) {
            if (RANDOM.nextInt(2) == 1) study.setConditioning(R01Study.CONDITION.POSITIVE.name());
            else study.setConditioning(R01Study.CONDITION.POSITIVE_NEGATION.name());
        } else if (type == CONDITION_TYPE.FIFTY_FIFTY) {
            if (RANDOM.nextInt(2) == 1) study.setConditioning(R01Study.CONDITION.FIFTY_FIFTY_RANDOM.name());
            else study.setConditioning(R01Study.CONDITION.FIFTY_FIFTY_BLOCKED.name());
        } else {
            study.setConditioning(R01Study.CONDITION.NEUTRAL.name());
        }
        return p;
    }


    @Override
    public List<Study> getStudies() {
        List<Study> studies = new ArrayList<>();
        studies.add(new R01Study());
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
        R01Study study = (R01Study)p.getStudy();

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
}