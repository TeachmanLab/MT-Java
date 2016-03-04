package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Table(name="RR")
@EqualsAndHashCode(callSuper = true)
@Data
public class RR extends QuestionnaireData {


    private int wedding1;
    private int wedding2;
    private int wedding3;
    private int wedding4;

    private int noise1;
    private int noise2;
    private int noise3;
    private int noise4;

    private int meeting_friend1;
    private int meeting_friend2;
    private int meeting_friend3;
    private int meeting_friend4;

    private int elevator1;
    private int elevator2;
    private int elevator3;
    private int elevator4;


    private int job1;
    private int job2;
    private int job3;
    private int job4;

    private int lunch1;
    private int lunch2;
    private int lunch3;
    private int lunch4;

    private int scrape1;
    private int scrape2;
    private int scrape3;
    private int scrape4;

    private int blood_test1;
    private int blood_test2;
    private int blood_test3;
    private int blood_test4;

    private int shopping1;
    private int shopping2;
    private int shopping3;
    private int shopping4;

}
