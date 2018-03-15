package org.mindtrails.domain.tracking;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.Participant;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Logs the date and time a text message was sent to a participant.
 */
@Entity
@Table(name="sms_log")
@Exportable
@DoNotDelete
@Data
public class SMSLog extends MindTrailsLog {


    private String message;
    private boolean successful = true;
    private String exception = "";

    public SMSLog() {}

    public SMSLog(Participant p, String message) {
        this.participant = p;
        this.message = message;
        this.dateSent  = new Date();
    }

    public SMSLog(Participant p, String message, Date date) {
        this.participant = p;
        this.message = message;
        this.dateSent  = date;
    }

    public void setError(Exception e) {
        this.successful = false;
        this.exception = e.getLocalizedMessage();
    }

}
