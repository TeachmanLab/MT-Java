package edu.virginia.psyc.r01.service;

import edu.virginia.psyc.r01.domain.R01Study;
import edu.virginia.psyc.r01.persistence.DASS21_AS;
import edu.virginia.psyc.r01.persistence.DASS21_ASRepository;
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
    DASS21_ASRepository dass21RRepository;

    @Override
    public Participant create() {
        Participant p = new Participant();
        R01Study study = new R01Study();
        p.setStudy(study);
        p.setReceiveGiftCards(true);

        List<R01Study.CONDITION> CONDITION_VALUES =
                Collections.unmodifiableList(Arrays.asList(R01Study.CONDITION.values()));

        R01Study.CONDITION type = CONDITION_VALUES.get(RANDOM.nextInt(CONDITION_VALUES.size()));
        study.setConditioning(type.name());

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
        List<DASS21_AS> forms = dass21RRepository.findBySessionId(session.getId());
        for (DASS21_AS e : forms) {
            if (e.eligible()) return true;
        }
        return false;
    }

    @Override
    public void saveNew(Participant p, HttpSession session) throws MissingEligibilityException {

        List<DASS21_AS> forms = dass21RRepository.findBySessionId(session.getId());
        if(forms.size() < 1) {
            throw new MissingEligibilityException();
        }

        save(p);
        R01Study study = (R01Study)p.getStudy();

        // Now that p is saved, connect any Expectancy Bias eligibility data back to the
        // session, and log it in the TaskLog
        for (DASS21_AS e : forms) {
            e.setParticipant(p);
            e.setSession("ELIGIBLE");
            dass21RRepository.save(e);
            study.completeEligibility(e);
        }
        save(p); // Re-save participant to record study.
    }
}