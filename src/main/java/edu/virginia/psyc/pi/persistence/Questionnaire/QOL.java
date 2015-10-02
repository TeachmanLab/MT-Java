package edu.virginia.psyc.pi.persistence.Questionnaire;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 */
@Entity
@Table(name="QOL")
@Data
public class QOL extends QuestionnaireData {

    private int material;
    private int health;
    private int relationships;
    private int children;
    private int spouse;
    private int friend;
    private int helping;
    private int participating;
    private int learning;
    private int understanding;
    private int work;
    private int expression;
    private int socializing;
    private int reading;
    private int recreation;
    private int independence;

}
