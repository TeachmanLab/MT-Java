package edu.virginia.psyc.mindtrails.persistence;

import edu.virginia.psyc.mindtrails.domain.tracking.ExportLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExportLogRepository extends JpaRepository<ExportLog, Long> {

    // Returns the lastest record.
    ExportLog findFirstByOrderByIdDesc();

}
