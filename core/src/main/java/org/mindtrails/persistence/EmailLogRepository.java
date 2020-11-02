package org.mindtrails.persistence;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tracking.EmailLog;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EmailLogRepository extends LogRepository<EmailLog> {
    Long countByDateSentAfter(Date date);

    Long countByEmailTypeAndParticipant(String emailType, Participant participant);
}
