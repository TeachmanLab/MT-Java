package edu.virginia.psyc.pi.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExportLogRepository extends JpaRepository<ExportLogDAO, Long> {

    // Returns the lastest record.
    ExportLogDAO findFirstByOrderByIdDesc();

}
