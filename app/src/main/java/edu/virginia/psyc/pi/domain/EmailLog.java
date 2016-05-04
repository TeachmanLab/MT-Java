package edu.virginia.psyc.pi.domain;

import edu.virginia.psyc.pi.service.EmailService;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/24/14
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailLog {

    private EmailService.TYPE type;
    private Date date;

    public EmailLog() {}

    public EmailLog(EmailService.TYPE type, Date date) {
        this.type = type;
        this.date = date;
    }


    public EmailService.TYPE getType() {
        return type;
    }

    public void setType(EmailService.TYPE type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "EmailLog{" +
                "type=" + type +
                ", date=" + date +
                '}';
    }
}
