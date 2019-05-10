package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.SMSLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SMSLogRepository extends LogRepository<SMSLog> {
    Long countByDateSentAfter(Date date);
}
