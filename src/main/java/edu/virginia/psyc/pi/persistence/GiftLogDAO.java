package edu.virginia.psyc.pi.persistence;

import com.fasterxml.jackson.annotation.*;
import edu.virginia.psyc.pi.domain.DoNotDelete;
import edu.virginia.psyc.pi.domain.Exportable;
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
public class GiftLogDAO implements Comparable<GiftLogDAO>{

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("participantId")
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

    @Override
    public int compareTo(GiftLogDAO o) {
        if(this.dateSent == null) return 0;
        return this.dateSent.compareTo(o.dateSent);
    }
}
