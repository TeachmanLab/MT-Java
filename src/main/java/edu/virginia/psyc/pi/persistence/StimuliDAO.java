package edu.virginia.psyc.pi.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 9:33 AM
 * A list of strings representing stimuli presented in a trialDAO.
 */
@Entity
@Table(name="stimuli")
@Data
public class StimuliDAO {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @Lob
    private String value;

    public StimuliDAO() {}

    public StimuliDAO(String s) {
        this.value = s;
    }
}
