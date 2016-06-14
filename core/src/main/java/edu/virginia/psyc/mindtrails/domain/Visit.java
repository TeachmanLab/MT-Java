package edu.virginia.psyc.mindtrails.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * A table for storing visits - likely related to "pixel tracking".  Where a remote
 * site requests an image from a web service, and we log that request.
 */
@Entity
@Table(name="Visit")
@Data
@Exportable
@DoNotDelete
public class Visit {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;
    private String name;
    private Date date;

    public Visit() {}

    public Visit(String name) {
        this.name = name;
        this.date = new Date();
    }

}
