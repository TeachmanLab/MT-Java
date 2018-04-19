package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {
    Long countByDateCompletedAfter(Date date);

    Integer countDistinctBySessionNameAndAndTaskName(String sessionName, String taskName);
}
