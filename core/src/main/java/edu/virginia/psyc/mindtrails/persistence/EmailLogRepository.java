package edu.virginia.psyc.mindtrails.persistence;

import edu.virginia.psyc.mindtrails.domain.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {}
