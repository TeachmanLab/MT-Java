package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.MissingDataLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface MissingDataLogRepository extends JpaRepository<MissingDataLog, Long> {

}