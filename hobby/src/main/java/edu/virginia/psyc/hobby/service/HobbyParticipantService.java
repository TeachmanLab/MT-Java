package edu.virginia.psyc.hobby.service;

import edu.virginia.psyc.hobby.domain.HobbyStudy;
import org.mindtrails.domain.Conditions.NoNewConditionException;
import org.mindtrails.domain.Conditions.RandomCondition;
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
public class HobbyParticipantService extends ParticipantServiceImpl implements ParticipantService {

    protected static final Random RANDOM = new Random();  // For generating random CBM and Prime values.

    @Autowired
    ParticipantRepository participantRepository;

    @Override
    public Participant create() {
        Participant p = new Participant();
        HobbyStudy study = new HobbyStudy();
        p.setStudy(study);

        return p;
    }

    @Override
    public List<Study> getStudies() {
        List<Study> studies = new ArrayList<>();
        studies.add(new HobbyStudy());
        return studies;
    }

    @Override
    public boolean isEligible(HttpSession session) {
        return true;
    }

    @Override
    public void saveNew(Participant p, HttpSession session) throws MissingEligibilityException {
        save(p);
        }

    @Override
    public RandomCondition getCondition(Participant p) throws NoNewConditionException {
        return null;
    }

    @Override
    public void markConditionAsUsed(RandomCondition rc) {

    }
}