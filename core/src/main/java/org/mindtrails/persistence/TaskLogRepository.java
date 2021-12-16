package org.mindtrails.persistence;

import org.mindtrails.domain.Study;
import org.mindtrails.domain.tracking.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {
    Long countByDateCompletedAfter(Date date);

    List findByDateCompletedGreaterThan(Date date);

    Integer countDistinctBySessionNameAndAndTaskName(String sessionName, String taskName);

    List<TaskLog> findDistinctByStudyInAndSessionNameAndTaskName(List<Study> studies, String sessionName, String taskName);

    TaskLog findByStudyAndSessionNameAndTaskName(Study study, String sessionName, String taskName);
}
