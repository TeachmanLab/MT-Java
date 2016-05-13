package edu.virginia.psyc.mindtrails.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by dan on 7/15/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    private String order_id;
    private String customer;
    private String account_identifier;
    private Recipient recipient;
    private String sku;
    private int amount;
    private boolean send_reward;
    private String reward_message;
    private String reward_subject;
    private String reward_from;
    private Reward reward;
    private String delivered_at;

    // Required by Jacson to deserialize this class.
    public Order() {}

    public Order(String customer, String account_identifier, String sku, int amount, boolean send_reward) {
        this.customer = customer;
        this.account_identifier = account_identifier;
        this.sku = sku;
        this.amount = amount;
        this.send_reward = send_reward;
    }

}
