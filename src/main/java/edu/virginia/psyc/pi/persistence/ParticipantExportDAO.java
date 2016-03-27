package edu.virginia.psyc.pi.persistence;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by dan on 3/24/16.
 * Data about a Participant that can be exported.
 */
@Data
@Entity
@Table(name="participant")
public class ParticipantExportDAO {

    @Id private long id;
    private String theme;
    private String study;
    private String cbmCondition;
    private String prime;
    private boolean admin;
    private boolean emailOptout;
    private boolean active;
    private boolean increase30;
    private Date   lastLogin;
    private String currentSession;
}
