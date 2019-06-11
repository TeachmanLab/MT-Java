package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.ImportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportLogRepository extends JpaRepository<ImportLog, Long> {
    ImportLog findFirstByScaleAndSuccessfulOrderByDateStartedDesc(String scale, boolean successful);
}
