package edu.virginia.psyc.pi.persistence.Questionnaire;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.virginia.psyc.pi.domain.Exportable;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;

/**
 * If questionnaire data should remain directly linked to a Partiicpant
 * (if for instance it is used in other business logic, and does not
 * contain sensitive data) then you can extend this class rather than
 * secureQuestionnaireData.
 */
@MappedSuperclass
@Data
@Exportable // All Quetionnaire data should be exportable.
public abstract class LinkedQuestionnaireData extends QuestionnaireData {

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true) // otherwise first ref as POJO, others as id
    @JsonProperty(value = "participant")
    protected ParticipantDAO participantDAO;

}
