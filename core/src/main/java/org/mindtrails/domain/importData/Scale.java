package org.mindtrails.domain.importData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Diheng on 12/26/2017;
 *
 *
 * */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scale {

    private String name;
    private long size;
    @JsonIgnore
    private long remoteSize;
    private boolean deleteable;

    public Scale() {}

    public Scale(String name, long size, boolean deleteable) {
        this.name = name;
        this.size = size;
        this.deleteable = deleteable;
    }

}
