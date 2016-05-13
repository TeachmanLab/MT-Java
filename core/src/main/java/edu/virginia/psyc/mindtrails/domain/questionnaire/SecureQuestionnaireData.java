package edu.virginia.psyc.mindtrails.domain.questionnaire;

import edu.virginia.psyc.mindtrails.domain.Exportable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
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



}
