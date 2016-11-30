package org.mindtrails.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by dan on 7/13/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundAccount {

    private final String customer;
    private final String account_identifier;
    private final int amount;
    private final String client_ip;
    private final String security_code;
    private final String cc_token;


}
