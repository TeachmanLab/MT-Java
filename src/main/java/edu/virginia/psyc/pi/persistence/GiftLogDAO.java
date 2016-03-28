package edu.virginia.psyc.pi.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.virginia.psyc.pi.domain.DoNotDelete;
import edu.virginia.psyc.pi.domain.Exportable;
import edu.virginia.psyc.pi.service.EmailService;
import lombok.Data;

import javax.persistence.*;
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
public class GiftLogDAO {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private ParticipantDAO participantDAO;
    private String orderId;
    private String sessionName;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE, dd MMM yyyy HH:mm:ss Z", timezone="EST")
    private Date dateSent;
    public GiftLogDAO() {};



    public GiftLogDAO(ParticipantDAO participantDAO, String orderId, String sessionName) {
        this.participantDAO = participantDAO;
        this.orderId = orderId;
        this.sessionName = sessionName;
        this.dateSent = new Date();
    }
}
