package edu.virginia.psyc.r34.service;

import edu.virginia.psyc.r34.domain.R34NeutralStudy;
import edu.virginia.psyc.r34.domain.R34Study;
import edu.virginia.psyc.r34.domain.R34Participant;
import edu.virginia.psyc.r34.persistence.PiParticipantRepository;
import edu.virginia.psyc.r34.persistence.Questionnaire.DASS21_AS;
import edu.virginia.psyc.r34.persistence.Questionnaire.DASS21_ASRepository;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.ParticipantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class R34ParticipantService extends ParticipantServiceImpl implements ParticipantService {

    protected static final Random RANDOM = new Random();  // For generating random CBM and Prime values.

    @Autowired
    PiParticipantRepository repository;
    @Autowired
    DASS21_ASRepository dass21_asRepository;

    List<R34Participant.CONDITION> CONDITION_VALUES =
            Collections.unmodifiableList(Arrays.asList(R34Participant.CONDITION.values()));
    List<R34Participant.PRIME> PRIME_VALUES =
            Collections.unmodifiableList(Arrays.asList(R34Participant.PRIME.values()));


    @Override
    public R34Participant create() {
        R34Participant p = new R34Participant();

        // Set the randome condition and prime.
        p.setCondition(CONDITION_VALUES.get(RANDOM.nextInt(CONDITION_VALUES.size())));
        p.setPrime(PRIME_VALUES.get(RANDOM.nextInt(PRIME_VALUES.size())));

        if(p.getCondition() == R34Participant.CONDITION.NEUTRAL) {
           p.setStudy(new R34NeutralStudy(R34Study.NAME.PRE.toString(), 0, null, new ArrayList<TaskLog>(), this.receiveGiftCards()));
        } else {
            p.setStudy(new R34Study(R34Study.NAME.PRE.toString(), 0, null, new ArrayList<TaskLog>(), this.receiveGiftCards()));
        }
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

        // Now that p is saved, connect any DASS21 eligibility data back to the
        // session.
        List<DASS21_AS> forms = dass21_asRepository.findBySessionId(session.getId());
        for(DASS21_AS dass21_as: forms) {
            dass21_as.setParticipant(p);
            dass21_as.setSession("ELIGIBLE");
            dass21_asRepository.save(dass21_as);
        }
    }

    @Override
    public void save(Participant p) {
        if(p instanceof R34Participant) {
            repository.save((R34Participant)p);
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
