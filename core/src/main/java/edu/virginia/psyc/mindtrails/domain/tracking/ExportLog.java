package edu.virginia.psyc.mindtrails.domain.tracking;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: dan
 * Date: 7/24/14
 * Time: 9:33 AM
 * Logs when a top level export request is executed, along with the total number of records
 * available at that time.
 */
@Entity
@Table(name="export_log")
@Data
public class ExportLog {

    @Id
    @GeneratedValue
    private int id;
    private Date date;
    private int totalRecords;

    public ExportLog() {}

    public ExportLog(int totalRecords) {
        this.totalRecords = totalRecords;
        this.date = new Date();
    }

}
