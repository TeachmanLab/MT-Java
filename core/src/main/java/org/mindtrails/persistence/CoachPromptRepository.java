package org.mindtrails.persistence;

import org.mindtrails.domain.CoachPrompt;
import org.mindtrails.domain.Participant;

import java.util.List;

public interface CoachPromptRepository extends QuestionnaireRepository<CoachPrompt> {

    List<CoachPrompt> findAllByParticipant(Participant p);
}
