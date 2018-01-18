package org.mindtrails.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.mindtrails.domain.DoNotDelete;
import org.mindtrails.domain.Exportable;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by dan on 3/24/16.
 * Data about a Participant that can be exported.
 */
@Data
@Entity
@Table(name="participant")
@Exportable
@DoNotDelete
public class ParticipantExportDAO {

    @Id private long id;
    private String theme;
    private boolean testAccount;
    private boolean admin;
    private boolean emailReminders;
    private boolean phoneReminders;
    private String timezone;
    private boolean active;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE, dd MMM yyyy HH:mm:ss Z", timezone="EST")
    private Date   lastLoginDate;
    private boolean receiveGiftCards;
    private String reference;
    private String campaign;
    private boolean over18;

}
