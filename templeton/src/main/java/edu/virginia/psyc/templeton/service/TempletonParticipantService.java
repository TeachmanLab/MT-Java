package edu.virginia.psyc.templeton.service;

import edu.virginia.psyc.templeton.domain.TempletonStudy;
import edu.virginia.psyc.templeton.persistence.ExpectancyBias;
import edu.virginia.psyc.templeton.persistence.ExpectancyBiasRepository;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.ParticipantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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


    List<TempletonStudy.CONDITION> CONDITION_VALUES =
            Collections.unmodifiableList(Arrays.asList(TempletonStudy.CONDITION.values()));
    List<TempletonStudy.PRIME> PRIME_VALUES =
            Collections.unmodifiableList(Arrays.asList(TempletonStudy.PRIME.values()));

    @Override
    public Participant create() {
        Participant p = new Participant();
        TempletonStudy study = new TempletonStudy();
        p.setStudy(study);

        study.setPrime(PRIME_VALUES.get(RANDOM.nextInt(PRIME_VALUES.size())));
        study.setCondition(CONDITION_VALUES.get(RANDOM.nextInt(CONDITION_VALUES.size())));

        return p;
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

        // Now that p is saved, connect any Expectancy Bias eligibility data back to the
        // session.
        List<ExpectancyBias> forms = biasRepository.findBySessionId(session.getId());
        for (ExpectancyBias e : forms) {
            e.setParticipant(p);
            e.setSession("ELIGIBLE");
            biasRepository.save(e);
        }
    }

}