package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.domain.tango.Account;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/22/14
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TangoServiceTest {

    Participant participant;
    TangoGiftService service;

    @Before
    public void setup() {
        participant = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
        service     = new TangoGiftService();
    }

    @Test
    public void testGetAccountInformation() {
        Account account = service.getAccountInfo();
        assertTrue("Test accounts should have some money in it.", account.getAvailable_balance() > 0);
    }

}
