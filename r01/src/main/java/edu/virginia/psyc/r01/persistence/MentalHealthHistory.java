package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="MentalHealthHistory")
@EqualsAndHashCode(callSuper = true)
@Data
public class MentalHealthHistory extends LinkedQuestionnaireData {

    /*
    Drop the following tables:
    1.mental_health_disorders;
    2.mental_health_disorders_past;
    3.no_help_reason;
    4.helps_past;
    5.help;
    6.help_seeking;
    7.health_change;
    8.help_change;
    9.mental_health_history;
     */


    @ElementCollection
    @CollectionTable(name = "mental_health_disorders", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "disorder")
    private List<String> disorders;

    private int gad;
    private int ocd;
    private int panic_disorder;
    private int agoraphobia;
    private int ptsd;
    private int social_anxiety;
    private int phobias;
    private int dementia;
    private int substance_use_disorder;
    private int schizophrenia;
    private int depression;
    private int bipolar;
    private int eating;
    private int personality;
    private int disorders_other;
    private String disorders_other_text;

    @ElementCollection
    @CollectionTable(name = "mental_health_help", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "help")
    private List<String> help;

    private int therapy;
    private int medications;
    private int social_support;
    private int self_help;
    private int otc_medications;
    private String other_help_text;
    private int other_help;
    private String help_other_text = "";

    @ElementCollection
    @CollectionTable(name = "mental_health_change_help", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "change_help")
    private List<String> change_help;
    private String change_help_text;

    @ElementCollection
    @CollectionTable(name = "mental_health_why_no_help", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "why_no_help")
    private List<String> why_no_help;




}


