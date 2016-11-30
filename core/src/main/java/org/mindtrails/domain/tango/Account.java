package org.mindtrails.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by dan on 7/13/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private String identifier;
    private String email;
    private String customer;
    private int available_balance;

}
