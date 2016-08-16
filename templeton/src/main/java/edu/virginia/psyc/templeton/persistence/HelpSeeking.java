package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="HelpSeeking")
@EqualsAndHashCode(callSuper = true)
@Data
public class HelpSeeking extends SecureQuestionnaireData {
    private boolean MorePerson;
    private boolean LessPerson;
    private boolean MoreMeds;
    private boolean LessMeds;
    private boolean MoreApps;
    private boolean LessApps;
    private boolean OtherChange;
    private String other;
    private boolean NoChange;

}

