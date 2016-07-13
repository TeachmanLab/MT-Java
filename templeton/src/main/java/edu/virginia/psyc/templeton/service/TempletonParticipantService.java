package edu.virginia.psyc.templeton.service;

import edu.virginia.psyc.templeton.domain.TempletonParticipant;
import edu.virginia.psyc.templeton.domain.TempletonStudy;
import edu.virginia.psyc.templeton.persistence.TempletonParticipantRepository;
import org.mindtrails.domain.Participant;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        return repository.findByEmail(p.getName());
    }

    @Override
    public Participant findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Participant findOne(long id) { return repository.findOne(id); };

    @Override
    public Page findAll(PageRequest pageRequest) {return repository.findAll(pageRequest);}

    @Override
    public Page search(String search, PageRequest pageRequest) {
        return repository.search(search, pageRequest);
    }

    @Override
    public void saveNew(Participant p, HttpSession session) {
        save(p);
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
