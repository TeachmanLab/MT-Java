package org.mindtrails.persistence;

import org.mindtrails.domain.ActionSequence.ActionSequence;
import org.mindtrails.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionSequenceRepository {


    // Dan - should we order by latency for any of these?
    List<ActionSequence> findAllByParticipantAndSessionNameAndTaskName(Participant participant, String sessionName, String taskName);
    List<ActionSequence> findAllByParticipant(Participant participant);
    
    // Dan - Do we need the following, and if so, should we sort by descending? This would be so we could identify the questions with the highest latency in a particular questionnaire
    // List<ActionSequence> findAllByStudyNameAndTaskNameOrderByLatency(String studyName, String taskName)
}

