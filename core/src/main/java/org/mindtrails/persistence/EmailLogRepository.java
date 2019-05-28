package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EmailLogRepository extends LogRepository<EmailLog> {
    Long countByDateSentAfter(Date date);
}
