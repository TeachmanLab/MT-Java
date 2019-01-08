package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.CoachLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachLogRepository extends JpaRepository<CoachLog, Long> {}
