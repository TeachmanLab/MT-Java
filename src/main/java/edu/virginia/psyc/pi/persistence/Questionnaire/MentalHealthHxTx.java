package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="MentalHealthHxTx")
@Data
public class MentalHealthHxTx implements QuestionnaireData {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private String OtherDesc;
    private String OtherDescNo;

    @ElementCollection
    @CollectionTable(name = "mental_health_disorders", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "disorder")
    private List<String> disorders;

    @ElementCollection
    @CollectionTable(name = "mental_health_disorders_no", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "Disorder_no")
    private List<String> disorders_no;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mentalHealthHxTx")
    private List<MentalHealthHelpful> helpful;

}

