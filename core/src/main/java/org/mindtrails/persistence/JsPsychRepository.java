package org.mindtrails.persistence;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JsPsychRepository extends JpaRepository<JsPsychTrial, Long> {

    public List<JsPsychTrial> findAllByParticipantAndStudyAndSession(long participant,
                                                                       String study, String session);
    List<JsPsychTrial> findDistinctByParticipantIn(List<Long> participants);

    List<JsPsychTrial> findDistinctByParticipantInAndSession(List<Long> participants, String session);
}

