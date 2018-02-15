package org.mindtrails.domain.questionnaire;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;
import org.mindtrails.domain.Exportable;
import org.mindtrails.domain.Participant;
import lombok.Data;
import org.mindtrails.domain.hasParticipant;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created a secure Questionnaire with an ecnrypted link to the Participant.
 */
@MappedSuperclass
@Data
@Exportable // All Quetionnaire data should be exportable.
public abstract class QuestionnaireData implements hasParticipant {


    @Id
    @GenericGenerator(name = "QUESTION_GEN", strategy = "org.mindtrails.persistence.MindtrailsIdGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "table_name", value = "ID_GEN"),
            @org.hibernate.annotations.Parameter(name = "value_column_name", value = "GEN_VAL"),
            @org.hibernate.annotations.Parameter(name = "segment_column_name", value = "GEN_NAME"),
            @org.hibernate.annotations.Parameter(name = "segment_value", value = "question") })
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "QUESTION_GEN")
    protected Long id;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE, dd MMM yyyy HH:mm:ss Z", timezone="EST")
    protected Date date;
    protected String session;

    protected String tag; // Optional additional data defined by a task definition

    protected double timeOnPage; // Time spend on page in seconds.

    /**
     * Override this method to add custom information that should
     * be passed through to your html web form before it is displayed.
     * @param p Participant.
     */
    public Map<String,Object> modelAttributes(Participant p) {
        return new HashMap<>();
    }

}
