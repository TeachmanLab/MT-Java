package org.mindtrails.domain.questionnaire;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.mindtrails.domain.Exportable;
import lombok.Data;
import org.mindtrails.domain.Participant;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Created a secure Questionnaire with an ecnrypted link to the Participant.
 */
@MappedSuperclass
@Data
@Exportable // All Quetionnaire data should be exportable.
public abstract class SecureQuestionnaireData extends QuestionnaireData {

    // An encrypted link to the participant;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String participantRSA;

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true) // otherwise first ref as POJO, others as id
    @JsonProperty(value = "participant")
    protected Participant participant;



}
