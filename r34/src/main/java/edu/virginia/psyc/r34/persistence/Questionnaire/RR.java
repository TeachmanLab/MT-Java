package edu.virginia.psyc.r34.persistence.Questionnaire;

import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

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
public class RR extends SecureQuestionnaireData {


    private int wedding_PS;
    private int wedding_NS;
    private int wedding_PF;
    private int wedding_NF;

    private int noise_PS;
    private int noise_NS;
    private int noise_PF;
    private int noise_NF;

    private int meeting_friend_PS;
    private int meeting_friend_NS;
    private int meeting_friend_PF;
    private int meeting_friend_NF;

    private int elevator_PS;
    private int elevator_NS;
    private int elevator_PF;
    private int elevator_NF;


    private int job_PS;
    private int job_NS;
    private int job_PF;
    private int job_NF;

    private int lunch_PS;
    private int lunch_NS;
    private int lunch_PF;
    private int lunch_NF;

    private int scrape_PS;
    private int scrape_NS;
    private int scrape_PF;
    private int scrape_NF;

    private int blood_test_PS;
    private int blood_test_NS;
    private int blood_test_PF;
    private int blood_test_NF;

    private int shopping_PS;
    private int shopping_NS;
    private int shopping_PF;
    private int shopping_NF;

}
