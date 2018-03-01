package edu.virginia.psyc.r34.persistence.Questionnaire;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
<<<<<<< HEAD

=======
>>>>>>> upstream/master
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by Diheng on 7/27/17.
 */

@Entity
@Table(name="ReturnIntention")
@EqualsAndHashCode(callSuper = true)
@Data
public class ReturnIntention extends LinkedQuestionnaireData {
    private int returnIntention;
<<<<<<< HEAD

    @Lob

=======
    @Lob
>>>>>>> upstream/master
    private String notReturnReason;
}