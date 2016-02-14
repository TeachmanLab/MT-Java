package edu.virginia.psyc.pi.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 9:33 AM
 * Basically a list of strings representing media shown
 * to the participant.
 */
@Entity
@Table(name="media")
@Data
public class MediaDAO {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @Lob
    private String value;

    public MediaDAO() {}
    public MediaDAO(String s) {
        this.value = s;
    }
}
