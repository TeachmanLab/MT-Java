package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="GrowthMindset")
@EqualsAndHashCode(callSuper = true)
@Data
/**
 * 1) You can learn new things, but you can't really change how you think.
 2) You can always change basic things about the kind of person you are.
 3) No matter how much you have been thinking a particular way, you can always change it quite a bit.
 4) You can do things differently, but the important parts of who you are can't really be changed.
 5) No matter what kind of person you are, you can always change substantially.
 6) You are a certain kind of person, and there is not much that can really be done to change that.
 7) You can always substantially change how you think.
 8) Your thinking style is something very basic about you that can't change very much.
 Responses use a 4 point Likert scale from 1 (Strongly Agree) to 4 (Strongly Disagree).
 */
public class GrowthMindset extends SecureQuestionnaireData {
    private int learn;
    private int basic;
    private int particularThinking;
    private int importantParts;
    private int substantially;
    private int certainKindOfPerson;
    private int alwaysChangeThinking;
    private int style;
}

