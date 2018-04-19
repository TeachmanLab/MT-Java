package org.mindtrails.domain;

import lombok.Data;

@Data
public class countMap {
        String name;
        Integer counts;

        countMap(String name, Integer counts) {
            this.name = name;
            this.counts = counts;
        }
}
