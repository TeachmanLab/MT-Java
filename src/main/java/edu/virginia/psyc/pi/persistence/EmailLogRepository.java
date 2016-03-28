package edu.virginia.psyc.pi.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLogDAO, Long> {}
