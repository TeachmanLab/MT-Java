package org.mindtrails.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    private String accountIdentifier;
    private String customerIdentifier;
    private int amount;
    private boolean sendEmail;
    private String utid;

    public Order() {}

    public Order(String accountIdentifier, String customerIdentifier, String utid, int amount, boolean sendEmail) {
        this.accountIdentifier = accountIdentifier;
        this.customerIdentifier = customerIdentifier;
        this.utid = utid;
        this.amount = amount;
        this.sendEmail = sendEmail;
    }

}
