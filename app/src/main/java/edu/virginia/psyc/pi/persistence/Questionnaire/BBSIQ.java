package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="BBSIQ")
@EqualsAndHashCode(callSuper = true)
@Data
public class BBSIQ extends SecureQuestionnaireData {

    private int visitors_outstay;
    private int visitors_engagement;
    private int visitors_bored;

    private int breath_flu;
    private int breath_suffocate;
    private int breath_physically;

    private int vision_strained;
    private int vision_glasses;
    private int vision_illness;

    private int shop_bored;
    private int shop_concentrating;
    private int shop_irritating;

    private int lightheaded_faint;
    private int lightheaded_eat;
    private int lightheaded_sleep;

    private int smoke_house;
    private int smoke_food;
    private int smoke_cig;

    private int friend_helpful;
    private int friend_incompetent;
    private int friend_moreoften;

    private int chest_indigestion;
    private int chest_sore;
    private int chest_heart;

    private int jolt_dream;
    private int jolt_burglar;
    private int jolt_wind;

    private int party_hear;
    private int party_boring;
    private int party_preoccupied;

    private int heart_active;
    private int heart_wrong;
    private int heart_excited;

    private int confused_outofmind;
    private int confused_cold;
    private int confused_work;

    private int urgent_junk;
    private int urgent_bill;
    private int urgent_died;

    private int dizzy_overtired;
    private int dizzy_ate;
    private int dizzy_ill;

}