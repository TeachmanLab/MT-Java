package org.mindtrails.persistence;

import java.util.List;

public interface ReasonsForEndingRepository extends QuestionnaireRepository<ReasonsForEnding> {
    List<ReasonsForEnding> findByParticipant(Long id);
}
