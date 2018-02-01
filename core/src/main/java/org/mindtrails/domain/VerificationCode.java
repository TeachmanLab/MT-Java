package org.mindtrails.domain;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;


import javax.persistence.*;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/29/14
 * Time: 3:15 PM
 * A token used to reset a password.  Only effective for a day.  Should be only one per participant.
 */
@Entity
@Table(name="verification_code")
@Data
public class VerificationCode {

    private static final int CODE_LENGTH = 6;

    @Id
    @GeneratedValue
    private int     id;

    @OneToOne
    private Participant participant;
    private Date    dateCreated;
    private String  code;

    /**
     * Creates a new Password token with a random string and a date/time of right now.
     */
    public VerificationCode() {
        this.code = RandomStringUtils.randomNumeric(CODE_LENGTH);
        this.dateCreated   = new Date();
    }
    public VerificationCode(Participant p) {
        this.participant = p;
        this.code = RandomStringUtils.randomNumeric(CODE_LENGTH);
        this.dateCreated   = new Date();
    }
    public VerificationCode(Participant p, Date dateCreated, String code) {
        this.participant = p;
        this.dateCreated = dateCreated;
        this.code = code;
    }

    /**
     * Returns true if this verification code was generated within one hour.
     * @return
     */
    public boolean valid() {
        DateTime created;
        DateTime now;

        created = new DateTime(this.dateCreated);
        now     = new DateTime();

        //return (Hours.hoursBetween(created, now).getHours() < 1 ||(Hours.hoursBetween(created, now).getHours() ==1 && Minutes.minutesBetween(created, now).getMinutes()<0));
        return (Minutes.minutesBetween(created, now).getMinutes()<60);
    }
}

