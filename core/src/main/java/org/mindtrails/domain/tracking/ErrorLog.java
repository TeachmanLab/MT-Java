package org.mindtrails.domain.tracking;

import lombok.Data;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.Participant;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: dan
 * Date: 7/24/14
 * Time: 9:33 AM
 * Logs errors encountered by the user during their session.
 */
@Entity
@Table(name="error_log")
@Exportable
@DoNotDelete
@Data
public class ErrorLog extends MindTrailsLog {

    private String errorType;
    private String errorMessage;
    private String requestedUrl;

    public ErrorLog() {};

    public ErrorLog(Participant participant, String requestedUrl, Exception e) {
        this.participant = participant;
        this.dateSent = new Date();
        this.requestedUrl = requestedUrl;
        this.errorType = e.getClass().getSimpleName();
        this.errorMessage = e.getMessage();

        // We store error messages as short strings, truncate to avoid mysql errors.
        if(errorMessage.length() > 255) errorMessage = errorMessage.substring(0,255);
    }

    public ErrorLog(Participant participant, String requestedUrl, String exceptionString) {
        this.participant = participant;
        this.dateSent = new Date();
        this.requestedUrl = requestedUrl;
        this.errorType = "ActionError (Failed AJAX POST request)";
        this.errorMessage = exceptionString;

        // We store error messages as short strings, truncate to avoid mysql errors.
        if(errorMessage.length() > 255) errorMessage = errorMessage.substring(0,255);
    }


}
