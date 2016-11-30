package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.ExportLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExportLogRepository extends JpaRepository<ExportLog, Long> {

    // Returns the lastest record.
    ExportLog findFirstByOrderByIdDesc();

}
