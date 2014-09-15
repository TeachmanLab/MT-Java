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
@Table(name="data")
public class DataDAO {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private TrialDAO trialDAO;

    @Column(name="myKey")
    private String key;

    @Lob
    private String value;

    public DataDAO() {}

    public DataDAO(String k, String v) {
        this.value = v;
        this.key   = k;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
