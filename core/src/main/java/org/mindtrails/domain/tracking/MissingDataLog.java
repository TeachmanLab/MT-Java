package org.mindtrails.domain.tracking;

import lombok.Data;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.Participant;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: dan
 * Date: 7/24/14
 * Time: 9:33 AM
 * Logs errors encountered by the user during their session.
 */
@Entity
@Table(name="missing_data_log")
@Exportable
@DoNotDelete
@Data
public class MissingDataLog extends MindTrailsLog {


    private String scale;
    private String columns;
    private long tableID;
    private String missingDate;

    public MissingDataLog() {};

    public MissingDataLog(Participant participant, String scale, String columns,
                          long tableID, String missingDate) {
        this.participant = participant;
        this.columns = columns;
        this.missingDate = missingDate;
        this.scale= scale;
        this.tableID = tableID;
        this.dateSent = new Date();
    }

}