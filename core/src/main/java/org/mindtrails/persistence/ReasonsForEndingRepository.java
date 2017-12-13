package org.mindtrails.persistence;

import java.util.List;

public interface ReasonsForEndingRepository extends QuestionnaireRepository<ReasonsForEnding> {
    List<ReasonsForEnding> findByParticipantId(Long id);
}
