package edu.virginia.psyc.pi.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

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
@Data
public class DataDAO {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @Column(name="myKey")
    private String key;

    @Lob
    private String value;

    public DataDAO() {}

    public DataDAO(String k, String v) {
        this.value = v;
        this.key   = k;
    }

}
