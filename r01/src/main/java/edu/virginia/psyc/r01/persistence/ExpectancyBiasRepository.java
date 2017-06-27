package edu.virginia.psyc.r01.persistence;

import edu.virginia.psyc.r01.persistence.ExpectancyBias;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.QuestionnaireRepository;

import java.util.List;


public interface ExpectancyBiasRepository extends QuestionnaireRepository<ExpectancyBias> {
    List<ExpectancyBias> findByParticipant(Participant p);
    List<ExpectancyBias> findBySessionId(String sessionId);
}
