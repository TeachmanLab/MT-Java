package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="RR")
public class RR implements QuestionnaireData {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    @Enumerated(EnumType.STRING)
    private Session.NAME session = Session.NAME.ELIGIBLE;

    private int yellow1;
    private int yellow2;
    private int yellow3;
    private int yellow4;

    private int coffee1;
    private int coffee2;
    private int coffee3;
    private int coffee4;

    private int elevator1;
    private int elevator2;
    private int elevator3;
    private int elevator4;

    private int flight1;
    private int flight2;
    private int flight3;
    private int flight4;

    private int job1;
    private int job2;
    private int job3;
    private int job4;

    private int noise1;
    private int noise2;
    private int noise3;
    private int noise4;


    private int restaurant1;
    private int restaurant2;
    private int restaurant3;
    private int restaurant4;

    public int getYellow1() {
        return yellow1;
    }

    public void setYellow1(int yellow1) {
        this.yellow1 = yellow1;
    }

    public int getYellow2() {
        return yellow2;
    }

    public void setYellow2(int yellow2) {
        this.yellow2 = yellow2;
    }

    public int getYellow3() {
        return yellow3;
    }

    public void setYellow3(int yellow3) {
        this.yellow3 = yellow3;
    }

    public int getYellow4() {
        return yellow4;
    }

    public void setYellow4(int yellow4) {
        this.yellow4 = yellow4;
    }

    public int getCoffee1() {
        return coffee1;
    }

    public void setCoffee1(int coffee1) {
        this.coffee1 = coffee1;
    }

    public int getCoffee2() {
        return coffee2;
    }

    public void setCoffee2(int coffee2) {
        this.coffee2 = coffee2;
    }

    public int getCoffee3() {
        return coffee3;
    }

    public void setCoffee3(int coffee3) {
        this.coffee3 = coffee3;
    }

    public int getCoffee4() {
        return coffee4;
    }

    public void setCoffee4(int coffee4) {
        this.coffee4 = coffee4;
    }

    public int getElevator1() {
        return elevator1;
    }

    public void setElevator1(int elevator1) {
        this.elevator1 = elevator1;
    }

    public int getElevator2() {
        return elevator2;
    }

    public void setElevator2(int elevator2) {
        this.elevator2 = elevator2;
    }

    public int getElevator3() {
        return elevator3;
    }

    public void setElevator3(int elevator3) {
        this.elevator3 = elevator3;
    }

    public int getElevator4() {
        return elevator4;
    }

    public void setElevator4(int elevator4) {
        this.elevator4 = elevator4;
    }

    public int getFlight1() {
        return flight1;
    }

    public void setFlight1(int flight1) {
        this.flight1 = flight1;
    }

    public int getFlight2() {
        return flight2;
    }

    public void setFlight2(int flight2) {
        this.flight2 = flight2;
    }

    public int getFlight3() {
        return flight3;
    }

    public void setFlight3(int flight3) {
        this.flight3 = flight3;
    }

    public int getFlight4() {
        return flight4;
    }

    public void setFlight4(int flight4) {
        this.flight4 = flight4;
    }

    public int getJob1() {
        return job1;
    }

    public void setJob1(int job1) {
        this.job1 = job1;
    }

    public int getJob2() {
        return job2;
    }

    public void setJob2(int job2) {
        this.job2 = job2;
    }

    public int getJob3() {
        return job3;
    }

    public void setJob3(int job3) {
        this.job3 = job3;
    }

    public int getJob4() {
        return job4;
    }

    public void setJob4(int job4) {
        this.job4 = job4;
    }

    public int getNoise1() {
        return noise1;
    }

    public void setNoise1(int noise1) {
        this.noise1 = noise1;
    }

    public int getNoise2() {
        return noise2;
    }

    public void setNoise2(int noise2) {
        this.noise2 = noise2;
    }

    public int getNoise3() {
        return noise3;
    }

    public void setNoise3(int noise3) {
        this.noise3 = noise3;
    }

    public int getNoise4() {
        return noise4;
    }

    public void setNoise4(int noise4) {
        this.noise4 = noise4;
    }

    public int getRestaurant1() {
        return restaurant1;
    }

    public void setRestaurant1(int restaurant1) {
        this.restaurant1 = restaurant1;
    }

    public int getRestaurant2() {
        return restaurant2;
    }

    public void setRestaurant2(int restaurant2) {
        this.restaurant2 = restaurant2;
    }

    public int getRestaurant3() {
        return restaurant3;
    }

    public void setRestaurant3(int restaurant3) {
        this.restaurant3 = restaurant3;
    }

    public int getRestaurant4() {
        return restaurant4;
    }

    public void setRestaurant4(int restaurant4) {
        this.restaurant4 = restaurant4;
    }

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

    public Session.NAME getSession() {
        return session;
    }

    public void setSession(Session.NAME session) {
        this.session = session;
    }


}