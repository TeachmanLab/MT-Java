package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.ActionLog;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tracking.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    Long countByDateRecordedAfter(Date date);

    public List findByDateRecordedGreaterThan(Date date);

//    Allows us to calculate avg latency of a particular task, across all participants
    List<ActionLog> findAllByStudyNameAndSessionNameAndTaskNameOrderByDateRecorded(String studyName, String sessionName, String taskName);

//    Allows us to calculate a single participant's avg latency within a particular task
    List<ActionLog> findAllByParticipantAndStudyNameAndSessionNameAndTaskNameOrderByDateRecorded(Participant participant, String studyName, String sessionName, String taskName);

//    Allows us to calculate a single participant's avg latency within a particular session (across all tasks)
    List<ActionLog> findAllByParticipantAndStudyNameAndSessionNameOrderByDateRecorded(Participant participant, String studyName, String sessionName);

    //    Allows us to calculate a single participant's avg latency within a particular study (across all tasks in all sessions)
    List<ActionLog> findAllByParticipantAndStudyNameOrderByDateRecorded(Participant participant, String studyName);

}

