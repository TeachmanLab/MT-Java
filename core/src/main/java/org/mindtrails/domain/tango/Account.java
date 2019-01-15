package org.mindtrails.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Account Structure returned from Tango gift card, version 2 of the api.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private String accountIdentifier;
    private String accountNumber;
    private String displayName;
    private String currencyCode;
    private int currentBalance;
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date createdAt;
    private String status;
    private String contactEmail;
}
