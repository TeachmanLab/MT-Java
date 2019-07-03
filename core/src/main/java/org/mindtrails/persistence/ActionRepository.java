package org.mindtrails.persistence;

import org.mindtrails.domain.Action.Action;
import org.mindtrails.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AngularTrainingRepository extends QuestionnaireRepository<Action> {}

