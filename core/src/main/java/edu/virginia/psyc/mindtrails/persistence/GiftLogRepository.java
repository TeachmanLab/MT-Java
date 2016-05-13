package edu.virginia.psyc.mindtrails.persistence;

import edu.virginia.psyc.mindtrails.domain.GiftLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftLogRepository extends JpaRepository<GiftLog, Long> {}
