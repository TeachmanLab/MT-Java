package org.mindtrails.persistence;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JsPsychRepository extends QuestionnaireRepository<JsPsychTrial> {

    List<JsPsychTrial> findAllByParticipantAndSession(Participant participant,
                                                              String session);
}

