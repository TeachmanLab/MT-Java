package org.mindtrails.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ActionSequence")
@Data
@Exportable
@DoNotDelete
public class ActionSequence {
    @ManyToOne
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true) // otherwise first ref as POJO, others as id

    private String action1;
    private String action2;
    private String studyName;
    private String sessionName;
    private String taskName;
    private double latency;

    @JsonProperty(value = "participant")
    protected Participant participant;
}




