package org.mindtrails.domain.AngularTraining;

import lombok.Data;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "AngularTraining")
@Data
@Exportable
public class AngularTraining extends LinkedQuestionnaireData {


    private String session;
    private Integer sessionIndex;
    private String sessionTitle;
    private String conditioning;
    private String study;
    private String stepTitle;
    private Integer stepIndex;
    private String sessionCounter;
    private String trialType;
    @Lob
    private String stimulus;
    private String stimulusName;
    private String buttonPressed;
    private boolean correct;
    private String device;
    private double rt;
    private double rtFirstReact;
    private double timeElapsed;

}




