package edu.virginia.psyc.mindtrails.MockClasses;

import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.persistence.ParticipantRepository;
import edu.virginia.psyc.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class TestParticipantService implements ParticipantService {

    @Autowired
    ParticipantRepository repository;


    @Override
    public Participant create() {
        return new Participant();
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
    }

    @Override
    public void save(Participant p) {
            repository.save(p);
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
