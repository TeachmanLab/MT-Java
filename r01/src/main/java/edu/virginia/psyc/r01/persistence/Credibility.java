package edu.virginia.psyc.r01.persistence;

/**
 * Created by Diheng on 4/27/17.
 */

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Credibility")
@EqualsAndHashCode(callSuper = true)
@Data
public class Credibility extends LinkedQuestionnaireData {
    private int important;
    private int confident_online;
    private int confident_design;
}

