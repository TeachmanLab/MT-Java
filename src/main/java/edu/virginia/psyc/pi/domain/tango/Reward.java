package edu.virginia.psyc.pi.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by dan on 7/15/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reward {
    private String token; // used by tango card to look up reward
    private String number; // the rewards card number.
    private String pin; // The rewards card pin.
    private String redemption_url;
    private String event_number;
}
