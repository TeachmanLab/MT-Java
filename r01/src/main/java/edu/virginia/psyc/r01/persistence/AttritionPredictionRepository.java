package edu.virginia.psyc.r01.persistence;

import org.mindtrails.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttritionPredictionRepository extends JpaRepository<AttritionPrediction,Long> {
}
