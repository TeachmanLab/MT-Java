package edu.virginia.psyc.pi.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by dan on 7/15/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse {
    private boolean success;
    private Account account;
}
