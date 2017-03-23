package org.mindtrails.persistence;

import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JsPsychRepository extends JpaRepository<JsPsychTrial, Long> {}
