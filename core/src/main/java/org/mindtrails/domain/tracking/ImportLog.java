package org.mindtrails.domain.tracking;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created after importing a set of records for a particular
 * scale or record
 */
@Entity
@Table(name="import_log")
@Data
public class ImportLog {

    @Id
    @GeneratedValue
    private int id;
    private Date dateStarted;
    private String scale;
    private int successCount;
    private int errorCount;
    private boolean successful = true;
    @Lob
    private String error;

    public ImportLog() {}

    public ImportLog(String scale) {
        this.successCount = 0;
        this.errorCount = 0;
        this.scale = scale;
        this.dateStarted = new Date();
    }

    public void incrementSuccess() {
        this.successCount++;
    }

    public void incrementError() {
        this.errorCount++;
        this.successful = false;
    }

    public void setError(String message) {
        this.successful = false;
        this.error = message;
    }

    /**
     * To help reduce the number of logs we write,
     * don't bother writing data if nothing happened.
     * @return
     */
    public boolean worthSaving() {
        return this.errorCount > 0 || this.successCount > 0 ||
                !this.isSuccessful();
    }
}
