package org.mindtrails.domain;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

/**
 * This comparator sorts a list of Employees by job title, age and salary
 * into ascending order.
 * @author www.codejava.net
 *
 */
public class EmailComparator implements Comparator<Email> {

    @Override
    public int compare(Email o1, Email o2) {
        return new CompareToBuilder()
                .append(o1.getScheduleType(), o2.getScheduleType())
                .append(o1.getSessions().toString(), o2.getSessions().toString())
                .append(o1.getDays().toString(), o2.getDays().toString()).toComparison();
    }
}