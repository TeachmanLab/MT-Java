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

    private int yellow1;
    private int yellow2;
    private int yellow3;
    private int yellow4;

    private int coffee1;
    private int coffee2;
    private int coffee3;
    private int coffee4;

    private int elevator1;
    private int elevator2;
    private int elevator3;
    private int elevator4;

    private int flight1;
    private int flight2;
    private int flight3;
    private int flight4;

    private int job1;
    private int job2;
    private int job3;
    private int job4;

    private int noise1;
    private int noise2;
    private int noise3;
    private int noise4;


    private int restaurant1;
    private int restaurant2;
    private int restaurant3;
    private int restaurant4;

}