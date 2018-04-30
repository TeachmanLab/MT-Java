package org.mindtrails.domain.questionnaire;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.Participant;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * If questionnaire data should remain directly linked to a Partiicpant
 * (if for instance it is used in other business logic, and does not
 * contain sensitive data) then you can extend this class rather than
 * LinkedQuestionnaireData.
 */
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@Exportable // All Quetionnaire data should be exportable.
public abstract class LinkedQuestionnaireData extends QuestionnaireData {

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true) // otherwise first ref as POJO, others as id
    @JsonProperty(value = "participant")
    protected Participant participant;


}
