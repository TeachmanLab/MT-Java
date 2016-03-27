package edu.virginia.psyc.pi.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantExportRepository extends JpaRepository<ExportLogDAO, Long> {

    // Returns the lastest record.
    ExportLogDAO findFirstByOrderByIdDesc();

}
