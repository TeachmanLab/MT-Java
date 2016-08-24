package edu.virginia.psyc.templeton.service;

import edu.virginia.psyc.templeton.domain.TempletonParticipant;
import edu.virginia.psyc.templeton.domain.TempletonStudy;
import edu.virginia.psyc.templeton.persistence.ExpectancyBias;
import edu.virginia.psyc.templeton.persistence.ExpectancyBiasRepository;
import edu.virginia.psyc.templeton.persistence.TempletonParticipantRepository;
import org.mindtrails.domain.Participant;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class TempletonParticipantService implements ParticipantService {

    protected static final Random RANDOM = new Random();  // For generating random CBM and Prime values.

    @Autowired
    TempletonParticipantRepository repository;

    @Autowired
    ExpectancyBiasRepository biasRepository;


    List<TempletonParticipant.CONDITION> CONDITION_VALUES =
            Collections.unmodifiableList(Arrays.asList(TempletonParticipant.CONDITION.values()));
    List<TempletonParticipant.PRIME> PRIME_VALUES =
            Collections.unmodifiableList(Arrays.asList(TempletonParticipant.PRIME.values()));

    @Override
    public Participant create() {
        TempletonParticipant p = new TempletonParticipant();
        p.setPrime(PRIME_VALUES.get(RANDOM.nextInt(PRIME_VALUES.size())));
        p.setConditioning(CONDITION_VALUES.get(RANDOM.nextInt(CONDITION_VALUES.size())));

        p.setStudy(new TempletonStudy());
        return p;
    }

    @Override
    public Participant get(Principal p) {
        if (p == null) return null;
        return repository.findByEmail(p.getName());
    }

    @Override
    public Participant findByEmail(String email) {
        return repository.findByEmail(email);
    }


    @Override
    public boolean isEligible(HttpSession session) {
        List<ExpectancyBias> forms = biasRepository.findBySessionId(session.getId());
        for(ExpectancyBias e: forms) {
            if(e.eligible()) return true;
        }
        return false;
    }

    @Override
    public void saveNew(Participant p, HttpSession session) {
        save(p);

        // Now that p is saved, connect any Expectancy Bias eligibility data back to the
        // session.
        List<ExpectancyBias> forms = biasRepository.findBySessionId(session.getId());
        for(ExpectancyBias e: forms) {
            e.setParticipant(p);
            e.setSession("ELIGIBLE");
            biasRepository.save(e);
        }
    }

    @Override
    public void save(Participant p) {

        if(p instanceof TempletonParticipant) {
            repository.save((TempletonParticipant)p);
        } else {
            throw new IllegalArgumentException("Participant must be a Templeton participant.");
        }
    }

    @Override
    public void flush() {
        repository.flush();
    }

    @Override
    public long count() {
        return repository.count();
    }
}
