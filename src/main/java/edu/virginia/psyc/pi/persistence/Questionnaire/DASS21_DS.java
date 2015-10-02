package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="DASS21_DS")
@Data
public class DASS21_DS extends QuestionnaireData {

    private int nopositive;
    private int difficult;
    private int nervous;
    private int blue;
    private int noenthusiastic;
    private int noworth;
    private int meaningless;

}