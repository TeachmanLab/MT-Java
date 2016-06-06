package edu.virginia.psyc.pi.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Restore the times that the image is requested.
 */
@Entity
@Table(name="Visit")
@Data
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
