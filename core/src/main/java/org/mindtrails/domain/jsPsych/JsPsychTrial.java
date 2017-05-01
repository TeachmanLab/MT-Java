package org.mindtrails.domain.jsPsych;

import lombok.Data;
import org.mindtrails.domain.Exportable;

import javax.persistence.*;

/**
 * Created by dan on 2/8/17.
 */
@Entity
@Table(name="JsPsychTrial")
@Data
@Exportable
public class JsPsychTrial {

    @TableGenerator(name = "QUESTION_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "QUESTION_GEN")
    private long id;

    private double rt;
    @Lob
    private String stimulus;
    private String button_pressed;
    private String trial_type;
    private int trial_index;
    private double time_elapsed;
    private String internal_node_id;
    private boolean correct;

    private long participantId;
    private String session;
    private String study;
    @Column (name = "conditioning")
    private String condition;
    private String device;

}
