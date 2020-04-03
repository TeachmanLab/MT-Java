package org.mindtrails.domain.tango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Account Structure returned from Tango gift card, version 2 of the api.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Catalog {

    private String catalogName;
    private List<Brand> brands;


    public List<Item> getItems() {
        return this.brands.stream().map(Brand::getItems).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Item findItemByCountryCode(String code) {
        for(Item i : this.getItems()) {
            if(i.countries.stream().anyMatch(code::equals)) {
                return i;
            }
        }
        throw new RuntimeException("no item with code:" + code);
    }
}

@Data
class Brand {
    String key;
    String brandName;
    List<Item> items;
}

