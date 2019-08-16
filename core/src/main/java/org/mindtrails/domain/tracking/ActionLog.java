package org.mindtrails.domain.tracking;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ActionLog")
@Data
@Exportable
@DoNotDelete
public class ActionLog {
    @Id
    @GenericGenerator(name = "ACTLOG_GEN", strategy = "org.mindtrails.persistence.MindtrailsIdGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "table_name", value = "ID_GEN"),
            @org.hibernate.annotations.Parameter(name = "value_column_name", value = "GEN_VAL"),
            @org.hibernate.annotations.Parameter(name = "segment_column_name", value = "GEN_NAME"),
            @org.hibernate.annotations.Parameter(name = "segment_value", value = "study") })
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ACTLOG_GEN")
    protected long id;

    private String actionName;
    private String studyName;
    private String sessionName;
    private String taskName;
    private Date dateRecorded;

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("participant")
    protected Participant participant;

    public ActionLog() {};

    public ActionLog(String actionName, String studyName, String sessionName,
                     String taskName, Participant participant) {
        this.actionName = actionName;
        this.studyName = studyName;
        this.sessionName = sessionName;
        this.taskName = taskName;
        this.dateRecorded = new Date();
        this.participant = participant;
    };
}




