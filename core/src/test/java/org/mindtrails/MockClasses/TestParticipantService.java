package org.mindtrails.MockClasses;

import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
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
        Participant p = new Participant();
        p.setStudy(new TestStudy());
        return p;
    }

    @Override
    public Participant get(Principal p) {
        if(p == null) return null;
        return repository.findByEmail(p.getName());
    }

    @Override
    public Participant findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public boolean isEligible(HttpSession session) {
        return false;
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
