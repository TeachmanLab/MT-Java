package edu.virginia.psyc.pi.persistence.Questionnaire;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;


/**
 * Created by Diheng on 9/8/15.
 */

@Entity
@Table(name="CIHS")
@EqualsAndHashCode(callSuper = true)
@Data
public class CIHS extends SecureQuestionnaireData {

    private String OtherDesc;
    @ElementCollection
    @CollectionTable(name = "change_in_help_seeking", joinColumns = @JoinColumn(name = "id"))
    @Column (name = "changes")
    private List<String> changes;

}
