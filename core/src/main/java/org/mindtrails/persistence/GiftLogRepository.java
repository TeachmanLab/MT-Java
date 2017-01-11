package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.GiftLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftLogRepository extends JpaRepository<GiftLog, Long> {}
