package org.mindtrails.domain.importData;

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
    private int size;
    private int localSize;
    private boolean deleteable;
}
