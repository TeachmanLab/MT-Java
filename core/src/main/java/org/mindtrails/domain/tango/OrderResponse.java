package org.mindtrails.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
        this.reward.setCredentials(new Credentials());
        this.getReward().getCredentials().setRedemptionLink(url);
        this.amountCharged = new AmountCharged();
        this.amountCharged.value = amount;
        this.amountCharged.total = amount;
    }

    @JsonIgnore
    public String getLink() {
        return this.getReward().getCredentials().getRedemptionLink();
    }

    @JsonIgnore
    public int getAmount() {
        return this.getAmountCharged().value;
    }

}

@Data
class AmountCharged {
    int value;
    String currencyCode;
    int total;
}


@Data
class Reward {
    private Credentials credentials;
    private String redemptionInstructions;
}

@Data
class Credentials {
    @JsonProperty("Redemption Link")
    private String redemptionLink;
}



