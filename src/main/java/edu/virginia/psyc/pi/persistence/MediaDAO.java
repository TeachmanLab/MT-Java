package edu.virginia.psyc.pi.persistence;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 9:33 AM
 * Basically a list of strings representing media shown to the participant..
 */
@Entity
@Table(name="media")
public class MediaDAO {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private TrialDAO trialDAO;

    private String value;

    public MediaDAO() {}

    public MediaDAO(String s) {
        this.value = s;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TrialDAO getTrialDAO() {
        return trialDAO;
    }

    public void setTrialDAO(TrialDAO trialDAO) {
        this.trialDAO = trialDAO;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
