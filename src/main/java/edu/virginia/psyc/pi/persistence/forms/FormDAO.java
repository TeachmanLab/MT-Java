package edu.virginia.psyc.pi.persistence.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.virginia.psyc.pi.domain.Exportable;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dan on 4/25/16.
 */
@Entity
@Table(name="FormSubmissions")
@Exportable
@Data
@JsonSerialize(using = FormSerializer.class)
public class FormDAO {

    // Use a lookup table so that id's will always increment upward
    // and be unique even as data is removed from the database.
    @TableGenerator(name = "QUESTION_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "QUESTION_GEN")
    protected Long id;

    // An encrypted link to the participant;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String participantRSA;

    protected Date date;
    protected String session;

    @Lob
    @JsonRawValue
    private String json;

    public FormDAO() {}

    public FormDAO(String participantRSA, String session, String json) {
        this.participantRSA = participantRSA;
        this.session = session;
        this.json = json;
        this.date = new Date();
    }


}
