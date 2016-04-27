package edu.virginia.psyc.pi.persistence.forms;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<FormDAO, Long> {}
