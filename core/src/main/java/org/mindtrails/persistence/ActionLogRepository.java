package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.ActionLog.ActionLog;
import org.mindtrails.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {

    public List findByDateGreaterThan(Date date);
    Long countByDateAfter(Date date);

    // TODO: Ask Dan if we need these anymore

    //    Allows us to calculate avg latency of a particular task, across all participants
    List<ActionLog> findAllByStudyNameAndSessionNameAndTaskNameOrderByParticipant(String studyName, String sessionName, String taskName);

//    Allows us to calculate a single participant's avg latency within a particular task
    List<ActionLog> findAllByParticipantAndStudyNameAndSessionNameAndTaskName(Participant participant, String studyName, String sessionName, String taskName);

//    Allows us to calculate a single participant's avg latency within a particular session (across all tasks)
    List<ActionLog> findAllByParticipantAndStudyNameAndSessionNameOrderByDate(Participant participant, String studyName, String sessionName);

    //    Allows us to calculate a single participant's avg latency within a particular study (across all tasks in all sessions)
    List<ActionLog> findAllByParticipantAndStudyNameOrderByDate(Participant participant, String studyName);

}

