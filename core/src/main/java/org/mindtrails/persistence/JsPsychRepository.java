package org.mindtrails.persistence;

import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JsPsychRepository extends JpaRepository<JsPsychTrial, Long> {

    public List<JsPsychTrial> findAllByParticipantAndStudyAndSession(long participant,
                                                                       String study, String session);
    @Query("SELECT COUNT (DISTINCT participant) FROM JsPsychTrial")
    Integer countDistinctByParticipant();
}

