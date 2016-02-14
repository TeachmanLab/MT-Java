package edu.virginia.psyc.pi.persistence.Questionnaire;

import lombok.Data;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


/**
 * Created by Diheng on 9/8/15.
 */

@Entity
@Table(name="CIHS")
@EqualsAndHashCode(callSuper = true)
@Data
public class CIHS extends QuestionnaireData {

    private String OtherDesc;
    @ElementCollection
    @CollectionTable(name = "change_in_help_seeking", joinColumns = @JoinColumn(name = "id"))
    @Column (name = "changes")
    private List<String> changes;

}
