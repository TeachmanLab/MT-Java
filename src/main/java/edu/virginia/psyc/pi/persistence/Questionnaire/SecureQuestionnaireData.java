package edu.virginia.psyc.pi.persistence.Questionnaire;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.virginia.psyc.pi.domain.CBMStudy;
import edu.virginia.psyc.pi.domain.Exportable;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
