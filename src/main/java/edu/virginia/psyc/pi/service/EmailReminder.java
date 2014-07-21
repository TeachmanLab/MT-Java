package edu.virginia.psyc.pi.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/21/14
 * Time: 2:36 PM
 * A Scheduled task that runs once daily at 2 am.  Based on a set of simple rules
 * it determines when it should send a reminder email.
 */
@Component
public class EmailReminder {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000) // every 5 seconds
    public void test() {
        System.out.println("The time is now " + dateFormat.format(new Date()));
    }

    @Scheduled(cron="0 0 2 * * *")  // schedules task for 2:00am every day.
    public void sendEmailReminder() {
        // Do something intelligent here.
    }



}
