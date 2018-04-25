package org.mindtrails.domain;

import lombok.Data;

@Data
public class countMap {
        String name;
        Long counts;
        Long duplicated;

        countMap(String name, Long counts, Long duplicated) {
            this.name = name;
            this.counts = counts;
            this.duplicated = duplicated;
        }
}

