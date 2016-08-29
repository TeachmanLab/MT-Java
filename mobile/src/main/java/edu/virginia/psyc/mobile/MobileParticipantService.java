package edu.virginia.psyc.mobile;

import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.ParticipantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * Largely a wrapper around the Participant Repository.  Allows us to
 * save, create, and find customized participant objects.
 */
@Service
public class MobileParticipantService extends ParticipantServiceImpl implements ParticipantService {


    @Autowired
    ParticipantRepository participantRepository;




    @Override
    public Participant create() {
        Participant p = new Participant();
        MobileStudy study = new MobileStudy();
        p.setStudy(study);

        return p;
    }


    @Override
    public boolean isEligible(HttpSession session) {
        return true;
    }

    @Override
    public void saveNew(Participant p, HttpSession session) {
        save(p);

    }

}