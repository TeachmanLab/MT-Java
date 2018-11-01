package org.mindtrails.domain;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTest {


    @Test
    public void testDateTimeFormatBeforeMyBrainMeltsAndMyEyesBleed() {
       String date="2018-10-31T15:15:00.000Z";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        LocalDateTime formatDateTime = LocalDateTime.parse(date, formatter);

        Assert.assertNotNull(formatDateTime);

    }

}