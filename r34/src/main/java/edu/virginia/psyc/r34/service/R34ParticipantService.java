package edu.virginia.psyc.r34.service;

import org.mindtrails.domain.Participant;
import org.mindtrails.service.ParticipantService;
import edu.virginia.psyc.r34.domain.CBMStudy;
import edu.virginia.psyc.r34.domain.PiParticipant;
import edu.virginia.psyc.r34.persistence.PiParticipantRepository;
import edu.virginia.psyc.r34.persistence.Questionnaire.DASS21_AS;
import edu.virginia.psyc.r34.persistence.Questionnaire.DASS21_ASRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class R34ParticipantService implements ParticipantService {

    @Autowired
    PiParticipantRepository repository;
    @Autowired
    DASS21_ASRepository dass21_asRepository;

    @Override
    public Participant create() {
        return new PiParticipant();
    }

    @Override
    public Participant get(Principal p) {
        return repository.findByEmail(p.getName());
    }

    @Override
    public Participant findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public void saveNew(Participant p, HttpSession session) {

        save(p);

        // Now that p is saved, connect any DASS21 eligibility data back to the
        // session.
        List<DASS21_AS> forms = dass21_asRepository.findBySessionId(session.getId());
        for(DASS21_AS dass21_as: forms) {
            dass21_as.setParticipant(p);
            dass21_as.setSession(CBMStudy.NAME.ELIGIBLE.toString());
            dass21_asRepository.save(dass21_as);
        }
    }

    @Override
    public void save(Participant p) {
        if(p instanceof PiParticipant) {
            repository.save((PiParticipant)p);
        } else {
            throw new IllegalArgumentException("Participant must be a r34 participant.");
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
