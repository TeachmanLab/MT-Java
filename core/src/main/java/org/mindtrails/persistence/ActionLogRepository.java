package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.ActionLog.ActionLog;
import org.mindtrails.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {

    List findByDateGreaterThan(Date date);
    Long countByDateAfter(Date date);


    //    Allows us to calculate a single participant's avg latency within a particular study (across all tasks in all sessions)
    List<ActionLog> findAllByParticipantAndStudyNameOrderByDate(Participant participant, String studyName);

}

