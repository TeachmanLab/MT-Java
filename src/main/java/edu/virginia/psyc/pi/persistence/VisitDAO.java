package edu.virginia.psyc.pi.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.virginia.psyc.pi.domain.DoNotDelete;
import edu.virginia.psyc.pi.domain.Exportable;
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
public class VisitDAO {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    private String name;
    private Date date;

    public VisitDAO() {}

    public VisitDAO(String name) {
        this.name = name;
        this.date = new Date();
    }

}
