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
 * Logs the date and time a particular gift card was awarded.
 */
@Entity
@Table(name="gift_log")
@Exportable
@DoNotDelete
@Data
public class GiftLog extends MindTrailsLog {

    private String orderId;
    private String sessionName;

    public GiftLog() {};

    public GiftLog(Participant participant, String orderId, String sessionName) {
        this.participant = participant;
        this.orderId = orderId;
        this.sessionName = sessionName;
        this.dateSent = new Date();
    }

}
