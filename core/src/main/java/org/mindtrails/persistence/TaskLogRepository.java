package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {}
