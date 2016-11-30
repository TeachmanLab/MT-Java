package org.mindtrails.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by dan on 7/15/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {
    private boolean success;
    private Order order;
}
