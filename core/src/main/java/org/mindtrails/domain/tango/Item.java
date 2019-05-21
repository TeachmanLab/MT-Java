package org.mindtrails.domain.tango;

import lombok.Data;

import java.util.List;

@Data
public class Item {
    String utid;
    String rewardName;
    String currencyCode;
    int minValue;
    int maxValue;
    List<String> countries;
}