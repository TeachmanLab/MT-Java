package org.mindtrails.domain.jsPsych;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.data.Exportable;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dan on 2/8/17.
 */
@Entity
@Table(name = "JsPsychTrial")
@Data
@Exportable
public class JsPsychTrial {

    @Id
    @GenericGenerator(name = "QUESTION_GEN", strategy = "org.mindtrails.persistence.MindtrailsIdGenerator", parameters = {
            @Parameter(name = "table_name", value = "ID_GEN"),
            @Parameter(name = "value_column_name", value = "GEN_VAL"),
            @Parameter(name = "segment_column_name", value = "GEN_NAME"),
            @Parameter(name = "segment_value", value = "trial") })
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "QUESTION_GEN")
    private long id;

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
    //TODO:  Create a constraint here that forces a connetion with the participant.
    private Long participant;
    private String session;
    private String study;
    @Column(name = "conditioning")
    private String condition;
    private String device;
    private Date dateSubmitted;

    public JsPsychTrial(Long id, String device, boolean correct) {
        this.participant = id;
        this.device = device;
        this.correct = correct;

    }

    public JsPsychTrial() {

    };

}
