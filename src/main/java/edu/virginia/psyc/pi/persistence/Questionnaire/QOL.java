package edu.virginia.psyc.pi.persistence.Questionnaire;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 */
@Entity
@Table(name="QOL")
public class QOL implements QuestionnaireData {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;

    @Enumerated(EnumType.STRING)
    private Session.NAME session = Session.NAME.ELIGIBLE;

    // 1. Define your form fields here using appropriate types.
    //    These should match exactly the "name" attribute on the
    //    the form elements created in Step 1.
    // -----------------------------------------------------

    private int material;
    private int health;
    private int relationships;
    private int children;
    private int spouse;
    private int friend;
    private int helping;
    private int participating;
    private int learning;
    private int understanding;
    private int work;
    private int expression;
    private int socializing;
    private int reading;
    private int recreation;
    private int independence;

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

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getRelationships() {
        return relationships;
    }

    public void setRelationships(int relationships) {
        this.relationships = relationships;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getSpouse() {
        return spouse;
    }

    public void setSpouse(int spouse) {
        this.spouse = spouse;
    }

    public int getFriend() {
        return friend;
    }

    public void setFriend(int friend) {
        this.friend = friend;
    }

    public int getHelping() {
        return helping;
    }

    public void setHelping(int helping) {
        this.helping = helping;
    }

    public int getParticipating() {
        return participating;
    }

    public void setParticipating(int participating) {
        this.participating = participating;
    }

    public int getLearning() {
        return learning;
    }

    public void setLearning(int learning) {
        this.learning = learning;
    }

    public int getUnderstanding() {
        return understanding;
    }

    public void setUnderstanding(int understanding) {
        this.understanding = understanding;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public int getExpression() {
        return expression;
    }

    public void setExpression(int expression) {
        this.expression = expression;
    }

    public int getSocializing() {
        return socializing;
    }

    public void setSocializing(int socializing) {
        this.socializing = socializing;
    }

    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
    }

    public int getRecreation() {
        return recreation;
    }

    public void setRecreation(int recreation) {
        this.recreation = recreation;
    }

    public int getIndepdence() {
        return independence;
    }

    public void setIndepdence(int indepdence) {
        this.independence = indepdence;
    }


}
