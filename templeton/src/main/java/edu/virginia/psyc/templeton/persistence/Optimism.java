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
@Table(name="Optimism")
@EqualsAndHashCode(callSuper = true)
@Data
public class Optimism extends SecureQuestionnaireData {
    private int UncertainTimes;
    private int EasyRelax;
    private int WrongWill;
    private int AlwaysOptimistic;
    private int EnjoyFriends;
    private int KeepBusy;
    private int HardlyEvery;
    private int EasilyUpset;
    private int GoodThings;
    private int Overall;

}

