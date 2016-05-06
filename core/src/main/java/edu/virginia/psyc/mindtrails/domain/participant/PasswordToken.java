package edu.virginia.psyc.mindtrails.domain.participant;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/29/14
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordToken {

    private static final int TOKEN_LENGTH = 20;

    private Date    dateCreated;
    private String  token;

    /**
     * Creates a new Password token with a random string and a date/time of right now.
     */
    public PasswordToken() {
        this.token = RandomStringUtils.randomAlphabetic(TOKEN_LENGTH);
        this.dateCreated   = new Date();
    }

    public PasswordToken(String token, Date date) {
        this.token         = token;
        this.dateCreated   = date;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
