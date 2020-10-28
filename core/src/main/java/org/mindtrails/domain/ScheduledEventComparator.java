package org.mindtrails.domain;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;
import java.util.List;

/**
 * This comparator sorts a list of Employees by job title, age and salary
 * into ascending order.
 * @author www.codejava.net
 *
 */
public class ScheduledEventComparator implements Comparator<ScheduledEvent> {

    Study study;

    public ScheduledEventComparator(Study study) {
        this.study = study;
    }

    @Override
    public int compare(ScheduledEvent o1, ScheduledEvent o2) {
        return new CompareToBuilder()
                .append(indexOfFirstSession(o1), indexOfFirstSession(o2))
                .append(leastDays(o1), leastDays(o2))
                .append(o1.getScheduleType(), o2.getScheduleType())
                .toComparison();
    }


    public int indexOfFirstSession(ScheduledEvent se) {
        if (se.getSessions().size() == 0) return -1;
        String first_session = se.getSessions().get(0);
        List<String> sessions = study.getSessionNames();
        for (int i = 0; i < sessions.size(); i++)
            if (sessions.get(i).equals(first_session))
                return i;
        return -1;
    }


    public int leastDays(ScheduledEvent se) {
        if (se.getDays().size() == 0) return -1;
        else return se.getDays().get(0);
    }

}