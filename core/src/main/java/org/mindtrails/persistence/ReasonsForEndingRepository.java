package org.mindtrails.persistence;

import org.mindtrails.domain.Participant;

import java.util.List;

public interface ReasonsForEndingRepository extends QuestionnaireRepository<ReasonsForEnding> {
    List<ReasonsForEnding> findByParticipant(Participant o);
}
