package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="DD")
public class DD implements QuestionnaireData {


    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private int monday_count;
    private int tuesday_count;
    private int wednesday_count;
    private int thursday_count;
    private int friday_count;
    private int saturday_count;
    private int sunday_count;

    private int monday_hours;
    private int tuesday_hours;
    private int wednesday_hours;
    private int thursday_hours;
    private int friday_hours;
    private int saturday_hours;
    private int sunday_hours;

    private int average_freq;
    private int average_amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParticipantDAO getParticipantDAO() {
        return participantDAO;
    }

    public void setParticipantDAO(ParticipantDAO participantDAO) {
        this.participantDAO = participantDAO;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getMonday_count() {
        return monday_count;
    }

    public void setMonday_count(int monday_count) {
        this.monday_count = monday_count;
    }

    public int getTuesday_count() {
        return tuesday_count;
    }

    public void setTuesday_count(int tuesday_count) {
        this.tuesday_count = tuesday_count;
    }

    public int getWednesday_count() {
        return wednesday_count;
    }

    public void setWednesday_count(int wednesday_count) {
        this.wednesday_count = wednesday_count;
    }

    public int getThursday_count() {
        return thursday_count;
    }

    public void setThursday_count(int thursday_count) {
        this.thursday_count = thursday_count;
    }

    public int getFriday_count() {
        return friday_count;
    }

    public void setFriday_count(int friday_count) {
        this.friday_count = friday_count;
    }

    public int getSaturday_count() {
        return saturday_count;
    }

    public void setSaturday_count(int saturday_count) {
        this.saturday_count = saturday_count;
    }

    public int getSunday_count() {
        return sunday_count;
    }

    public void setSunday_count(int sunday_count) {
        this.sunday_count = sunday_count;
    }

    public int getMonday_hours() {
        return monday_hours;
    }

    public void setMonday_hours(int monday_hours) {
        this.monday_hours = monday_hours;
    }

    public int getTuesday_hours() {
        return tuesday_hours;
    }

    public void setTuesday_hours(int tuesday_hours) {
        this.tuesday_hours = tuesday_hours;
    }

    public int getWednesday_hours() {
        return wednesday_hours;
    }

    public void setWednesday_hours(int wednesday_hours) {
        this.wednesday_hours = wednesday_hours;
    }

    public int getThursday_hours() {
        return thursday_hours;
    }

    public void setThursday_hours(int thursday_hours) {
        this.thursday_hours = thursday_hours;
    }

    public int getFriday_hours() {
        return friday_hours;
    }

    public void setFriday_hours(int friday_hours) {
        this.friday_hours = friday_hours;
    }

    public int getSaturday_hours() {
        return saturday_hours;
    }

    public void setSaturday_hours(int saturday_hours) {
        this.saturday_hours = saturday_hours;
    }

    public int getSunday_hours() {
        return sunday_hours;
    }

    public void setSunday_hours(int sunday_hours) {
        this.sunday_hours = sunday_hours;
    }

    public int getAverage_freq() {
        return average_freq;
    }

    public void setAverage_freq(int average_freq) {
        this.average_freq = average_freq;
    }

    public int getAverage_amount() {
        return average_amount;
    }

    public void setAverage_amount(int average_amount) {
        this.average_amount = average_amount;
    }
}