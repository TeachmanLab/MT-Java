package edu.virginia.psyc.r01.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConditionAssignmentSettingsRepository extends JpaRepository<ConditionAssignmentSettings, Long> {
    ConditionAssignmentSettings findFirstByOrderByLastModifiedDesc();

    List<ConditionAssignmentSettings> findAllByOrderByLastModifiedDesc();
}
