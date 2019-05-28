package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;





@Repository
public interface ErrorLogRepository extends LogRepository<ErrorLog> {
    Long countByDateSentAfter(Date date);
}
