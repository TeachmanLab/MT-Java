package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ExpectancyBias")
@EqualsAndHashCode(callSuper = true)
@Data
public class ExpectancyBias extends SecureQuestionnaireData {
    private int LongHealthy;
    private int DoctorRate;
    private int TerribleCondition;
    private int ShortRest;
    private int Reruns;
    private int VerySick;
    private int PotentialRelationship;
    private int ComeBack;
    private int EndUpAlone;
    private int SuggestPotential;
    private int Dishes;
    private int Argument;
    private int Bagel;
    private int SettleIn;
    private int Offend;
    private int LoseTouch;
    private int Boxes;
    private int MakePlans;
    private int Lunch;
    private int ConsideredAdvancement;
    private int Stuck;
    private int Phone;
    private int NotSelected;
    private int Meeting;
    private int Pinched;
    private int Saving;
    private int Thermostat;
    private int FinanciallySecure;
    private int Ruining;
    private int Food;
    private int KaraokeOften;
    private int MakeFun;
    private int GroceryStore;
    private int FallDown;
    private int BestTime;

}

