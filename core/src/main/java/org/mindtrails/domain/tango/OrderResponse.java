package org.mindtrails.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {
    private String referenceOrderID;
    private String utid;
    private String rewardName;
    private String status;
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date createdAt;
    private boolean success;
    private Reward reward;
    private AmountCharged amountCharged;

    public OrderResponse() {}

    public OrderResponse(String orderId, String utid, String rewardName, String status,
                         boolean success, String url, int amount) {
        this.referenceOrderID = orderId;
        this.createdAt = new Date();
        this.utid = utid;
        this.rewardName = rewardName;
        this.status = status;
        this.success = success;
        this.reward = new Reward();
        this.reward.setCredentialList(new ArrayList<Credential>());
        this.reward.getCredentialList().add(new Credential(url));
        this.amountCharged = new AmountCharged();
        this.amountCharged.value = amount;
        this.amountCharged.total = amount;
    }

    @JsonIgnore
    public String getLink() {
        return this.getReward().redemptionUrl();
    }

    @JsonIgnore
    public float getAmount() {
        return this.getAmountCharged().value;
    }

    @JsonIgnore
    public float getUsAmount() {
        return this.getAmountCharged().total;
    }

}

@Data
class AmountCharged {
    String currencyCode;
    float eschangeRate;
    float value;
    float fee;
    float total;
}


@Data
class Reward {
    private List<Credential> credentialList;
    private String redemptionInstructions;

    public String redemptionUrl() {
        for(Credential c: this.credentialList) {
            if(c.getCredentialType().equals(Credential.REDEMPTION_URL)) {
                return c.getValue();
            }
        }
        return ("");
    }

}

@Data
class Credential {
    public static final String REDEMPTION_URL = "redemptionUrl";

    private String credentialType;
    private String lable;
    private String value;

    public Credential() {}

    public Credential(String url) {
        this.credentialType = REDEMPTION_URL;
        this.value = url;
    }
}



