package edu.virginia.psyc.r01.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.HasReturnDate;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by Diheng on 7/27/17.
 */

@Entity
@Table(name="ReturnIntention")
@EqualsAndHashCode(callSuper = true)
@Data
public class ReturnIntention extends LinkedQuestionnaireData implements HasReturnDate {

    @Lob
    private String notReturnReason;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE, dd MMM yyyy HH:mm:ss Z", timezone="GMT")
    private Date returnDate;

    @Transient
    @JsonIgnore
    private String returnDateString;

    private Long daysTillReturning;


    /*
     * MASSIVE HACK, as after 4 hours, I was never able to get spring to correctly prase and set the
     * date using annotations or standard forms.  So just wait for this to get set, and convert it
     * correctly so it gets recorded in the database.  Everything else I tried was just silently
     * ignored.
    */
    public void setReturnDateString(String dateISO) {
        LocalDateTime localDateTime =
                LocalDateTime.parse(dateISO, DateTimeFormatter.ISO_DATE_TIME);
        this.returnDate =  Date.from(localDateTime.toInstant(ZoneOffset.UTC));

        LocalDateTime today = LocalDateTime.now(ZoneOffset.UTC);
        this.daysTillReturning = today.until( localDateTime, ChronoUnit.DAYS);
    }

}