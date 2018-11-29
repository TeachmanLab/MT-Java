package org.mindtrails.domain.Conditions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RandomConditionRepository extends JpaRepository<RandomCondition, Long> {

    RandomCondition findFirstBySegmentNameOrderById(String segment);

    long countAllBySegmentName(String segment);

}
