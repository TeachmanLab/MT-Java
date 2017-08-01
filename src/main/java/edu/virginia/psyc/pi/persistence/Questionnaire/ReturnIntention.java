package edu.virginia.psyc.pi.persistence.Questionnaire;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by Diheng on 7/27/17.
 */

@Entity
@Table(name="ReturnIntention")
@EqualsAndHashCode(callSuper = true)
@Data
public class ReturnIntention extends QuestionnaireData {
    private int returnIntention;
    @Lob
    private String notReturnReason;
}