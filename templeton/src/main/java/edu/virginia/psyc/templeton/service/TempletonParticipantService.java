package edu.virginia.psyc.templeton.service;

import edu.virginia.psyc.templeton.domain.TempletonStudy;
import edu.virginia.psyc.templeton.persistence.ExpectancyBias;
import edu.virginia.psyc.templeton.persistence.ExpectancyBiasRepository;
import org.mindtrails.domain.Participant;
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
public class TempletonParticipantService extends ParticipantServiceImpl implements ParticipantService {

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
        TempletonStudy study = new TempletonStudy();
        p.setStudy(study);

        CONDITION_TYPE type = CONDITION_VALUES.get(RANDOM.nextInt(CONDITION_VALUES.size()));
        if(type == CONDITION_TYPE.POSITIVE) {
            if (RANDOM.nextInt(2) == 1) study.setConditioning(TempletonStudy.CONDITION.POSITIVE);
            else study.setConditioning(TempletonStudy.CONDITION.POSITIVE_NEGATION);
        } else if (type == CONDITION_TYPE.FIFTY_FIFTY) {
            if (RANDOM.nextInt(2) == 1) study.setConditioning(TempletonStudy.CONDITION.FIFTY_FIFTY_RANDOM);
            else study.setConditioning(TempletonStudy.CONDITION.FIFTY_FIFTY_BLOCKED);
        } else {
            study.setConditioning(TempletonStudy.CONDITION.NEUTRAL);
        }

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
    public void saveNew(Participant p, HttpSession session) {
        save(p);

        TempletonStudy study = (TempletonStudy)p.getStudy();

        // Now that p is saved, connect any Expectancy Bias eligibility data back to the
        // session, and log it in the TaskLog
        List<ExpectancyBias> forms = biasRepository.findBySessionId(session.getId());
        for (ExpectancyBias e : forms) {
            e.setParticipant(p);
            e.setSession("ELIGIBLE");
            biasRepository.save(e);
            study.completeEligibility(e);
        }
        save(p); // Re-save participant to record study.
    }
}