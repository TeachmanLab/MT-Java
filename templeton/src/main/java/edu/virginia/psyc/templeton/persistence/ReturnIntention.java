package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

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
public class ReturnIntention extends SecureQuestionnaireData {
    private int returnIntention;

    @Lob
    private String notReturnReason;
}