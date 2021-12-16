package org.mindtrails.persistence;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.questionnaire.QuestionnaireData;
import org.mindtrails.domain.tracking.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;
import java.util.List;

@NoRepositoryBean
public interface QuestionnaireRepository<T extends QuestionnaireData>
        extends JpaRepository<T, Long> {
    List<T> findByIdGreaterThan(long id);

    List<T> findByDateGreaterThan(Date date);

    List<T> findDistinctByParticipantIn(List<Participant> participants);

    List<T> findDistinctByParticipantInAndSession(List<Participant> participants, String sessionName );

}