package org.mindtrails.domain.jsPsych;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.data.Exportable;
import org.hibernate.annotations.Parameter;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dan on 2/8/17.
 */
@Entity
@Table(name = "JsPsychTrial")
@Data
@Exportable
public class JsPsychTrial extends LinkedQuestionnaireData {

    private double rt;
    private double rt_firstReact;
    @Lob
    private String stimulus;
    private String button_pressed;
    private String trial_type;
    private int trial_index;
    private double time_elapsed;
    private String internal_node_id;
    private boolean correct;
    private String device;
    private Date dateSubmitted;

    public JsPsychTrial(Participant p, String device, boolean correct) {
        this.participant = p;
        this.device = device;
        this.correct = correct;

    }

    public JsPsychTrial() {

    };

}
