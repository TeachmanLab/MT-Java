package org.mindtrails.persistence;

import org.mindtrails.domain.tracking.ErrorLog;
import org.mindtrails.domain.tracking.MindTrailsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@NoRepositoryBean
public interface LogRepository<T extends MindTrailsLog> extends JpaRepository<T, Long> {

    public List<T> findByDateSentGreaterThan(Date date);
}
