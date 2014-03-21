package edu.virginia.psyc.pi.persistence;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 9:33 AM
 * A list of strings representing stimuli presented in a trialDAO.
 */
@Entity
public class StimuliDAO {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private TrialDAO trialDAO;

    private String value;

    public StimuliDAO() {}

    public StimuliDAO(String s) {
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
