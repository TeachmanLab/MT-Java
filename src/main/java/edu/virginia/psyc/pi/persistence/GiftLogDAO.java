package edu.virginia.psyc.pi.persistence;

import edu.virginia.psyc.pi.service.EmailService;

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
public class GiftLogDAO {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;

    private String orderId;
    private Date dateSent;

    public GiftLogDAO() {};

    public GiftLogDAO(ParticipantDAO participantDAO, String orderId) {
        this.participantDAO = participantDAO;
        this.orderId = orderId;
        this.dateSent = new Date();
    }

    /****************************************
     *   Generated Methods follow
     ******************************************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParticipantDAO getParticipantDAO() {
        return participantDAO;
    }

    public void setParticipantDAO(ParticipantDAO participantDAO) {
        this.participantDAO = participantDAO;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
}
