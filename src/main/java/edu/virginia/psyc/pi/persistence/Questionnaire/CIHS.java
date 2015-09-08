package edu.virginia.psyc.pi.persistence.Questionnaire;

import lombok.Data;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


/**
 * Created by Diheng on 9/8/15.
 */

@Entity
@Table(name="CIHS")
@Data
public class CIHS implements QuestionnaireData {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private String OtherDesc;
    @ElementCollection
    @CollectionTable(name = "change_in_help_seeking", joinColumns = @JoinColumn(name = "id"))
    @Column (name = "change")
    private List<String> changes;

}
