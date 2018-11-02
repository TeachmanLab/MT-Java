package org.mindtrails.domain;

import lombok.Data;
import org.thymeleaf.context.Context;

import java.util.Date;

/**
 * Represents an email sent to a participant or admin.
 */
@Data
public class Email {
    private final String type;  // This type should correspond to a template in /resources/templates/emails/
    private String subject;
    private String to;  // a valid email address
    private Context context; // Contextural data fro the template, if needed.
    private Participant participant;  // participant receiving this email, or that this email is about.
    private Date calendarDate;
    


    public Email (String type, String subject) {
        this.type = type;
        this.subject = subject;
    }

}
