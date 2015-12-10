package edu.virginia.psyc.pi.persistence;

import edu.virginia.psyc.pi.service.EmailService;
import lombok.Data;

import javax.persistence.*;
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
public class ExportLogDAO {

    @Id
    @GeneratedValue
    private int id;
    private Date date;
    private int totalRecords;


    public ExportLogDAO(int totalRecords) {
        this.totalRecords = totalRecords;
        this.date = new Date();
    }

}
