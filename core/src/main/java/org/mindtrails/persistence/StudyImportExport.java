package org.mindtrails.persistence;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.data.Exportable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.util.Date;


/**
 * Created by Diheng on Jan 16 2018.
 * Data about a study that can be exported and imported.  
 *
 * */
@Data
@Entity
@Table(name="study")
@Exportable
@DoNotDelete
public class StudyImportExport {
    @Id private long id;
    private String studyType;
    private String conditioning;
    private String currentSession;
    private String currentTaskIndex;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE, dd MMM yyyy HH:mm:ss Z", timezone="EST")
    private Date lastSessionDate;
    private boolean receiveGiftCards;
    private double increasePercent;
    private String studyExtension;
}
