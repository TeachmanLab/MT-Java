package org.mindtrails.domain;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.Hours;

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
@Table(name="password_token")
@Data
public class PasswordToken {

    private static final int TOKEN_LENGTH = 20;

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Participant participant;
    private Date    dateCreated;
    private String  token;

    /**
     * Creates a new Password token with a random string and a date/time of right now.
     */
    public PasswordToken() {
        this.token = RandomStringUtils.randomAlphabetic(TOKEN_LENGTH);
        this.dateCreated   = new Date();
    }

    public PasswordToken(Participant p, Date dateCreated, String token) {
        this.participant = p;
        this.dateCreated = dateCreated;
        this.token = token;
    }

    /**
     * Returns true if this Password Token was created in the last 24 hours.
     * @return
     */
    public boolean valid() {
        DateTime created;
        DateTime now;

        created = new DateTime(this.dateCreated);
        now     = new DateTime();

        return (Hours.hoursBetween(created, now).getHours() < 24);
    }
}
