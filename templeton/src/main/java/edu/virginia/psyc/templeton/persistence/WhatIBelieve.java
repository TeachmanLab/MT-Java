package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by saragomezt on 7/11/16.
 */

@Entity
@Table(name="WhatIBelieve")
@EqualsAndHashCode(callSuper = true)
@Data
public class WhatIBelieve extends SecureQuestionnaireData {

    // Originally a part of the NGSES
    private int difficultTasks;
    private int performEffectively;
    private int compared;

    // Originally from the MBP Growth Mindset
    private int learn;
    private int particularThinking;
    private int alwaysChangeThinking;

    // Originally from the LOT-R optimism scale
    private int wrongWill;
    private int hardlyEver;


}

