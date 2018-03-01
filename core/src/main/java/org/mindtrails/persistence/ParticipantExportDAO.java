package org.mindtrails.persistence;

import com.fasterxml.jackson.annotation.*;
import org.mindtrails.domain.BaseStudy;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.data.Exportable;
import lombok.Data;
import org.mindtrails.domain.Study;

import javax.persistence.*;
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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity=BaseStudy.class)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true) // otherwise first ref as POJO, others as id
    @JsonProperty(value = "study")
    protected Study study;


}
