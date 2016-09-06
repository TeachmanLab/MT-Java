package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="Mood")
@EqualsAndHashCode(callSuper = true)
@Data
public class Mood extends SecureQuestionnaireData {
    private int Dryness;
    private int DifficultyBreathing;
    private int Trembling;
    private int WorryPanic;
    private int PanicFelt;
    private int HeartAware;
    private int ScaredNoReason;
    private int NoPositiveFeeling;
    private int Initiative;
    private int NervousEnergy;
    private int Blue;
    private int Enthusiastic;
    private int Worth;
    private int Meaningless;


}

