package org.mindtrails.domain.questionnaire;

import lombok.Data;
import org.mindtrails.domain.data.Exportable;

/**
 * Created by dan on 10/23/15.
 */
@Data
public class ExportableInfo {
    private String name;
    private long   size;
    private boolean deleteable;

    public ExportableInfo() {}

    public ExportableInfo(String name, long size, boolean deleteable) {
        this.name = name;
        this.size = size;
        this.deleteable = deleteable;
    }
}
