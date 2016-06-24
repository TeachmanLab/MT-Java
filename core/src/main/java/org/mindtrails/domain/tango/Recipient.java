package org.mindtrails.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by dan on 7/15/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipient {
    private  String name;
    private  String email;

    public Recipient() {}

    public Recipient(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
