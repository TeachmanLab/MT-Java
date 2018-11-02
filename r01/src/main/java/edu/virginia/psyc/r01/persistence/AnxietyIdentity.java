package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="AnxietyIdentity")
@EqualsAndHashCode(callSuper = true)
@Data
public class AnxietyIdentity extends LinkedQuestionnaireData {

    private Integer anxietyIdentity;
}

