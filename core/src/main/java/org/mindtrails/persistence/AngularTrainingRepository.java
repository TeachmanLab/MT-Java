package org.mindtrails.persistence;

import org.mindtrails.domain.AngularTraining.AngularTraining;
import org.mindtrails.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AngularTrainingRepository extends QuestionnaireRepository<AngularTraining> {

    List<AngularTraining> findAllByParticipantAndSessionOrderByDate(Participant participant, String session);
    List<AngularTraining> findAllByParticipantAndSessionAndStepTitleOrderById(Participant participant, String session, String stepTitle);
    List<AngularTraining> findAllByParticipantAndTrialType(Participant participant, String trialType);

}

