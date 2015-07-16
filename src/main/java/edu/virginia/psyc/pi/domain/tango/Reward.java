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
    private String redemption_url; // link to provide to the user.
    private String event_number; // Not terrifically sure.


    public Reward() {};
    public Reward(String token, String number, String pin, String redemption_url, String event_number) {
        this.token = token;
        this.number = number;
        this.pin = pin;
        this.redemption_url = redemption_url;
        this.event_number = event_number;
    }

}
