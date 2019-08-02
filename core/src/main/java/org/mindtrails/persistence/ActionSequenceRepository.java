package org.mindtrails.persistence;

import org.mindtrails.domain.ActionSequence;
import org.mindtrails.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActionSequenceRepository extends JpaRepository<ActionSequence, Long> {

    Long countByDateCompletedAfter(Date date);

    public List findByDateCompletedGreaterThan(Date date);

    List<ActionSequence> findAllByParticipantAndSessionNameAndTaskName(Participant participant, String sessionName, String taskName);
    
    List<ActionSequence> findAllByParticipant(Participant participant);
    
    // Dan - Do we need the following, and if so, should we sort by descending? This would be so we could identify the questions with the highest latency in a particular questionnaire
    // List<ActionSequence> findAllByStudyNameAndTaskNameOrderByLatency(String studyName, String taskName)
}

