package edu.virginia.psyc.mindtrails.domain.participant;

import lombok.Data;
import java.util.Date;

/**
 * For recording a gift.  This is very minimal because we can request all the additional details from the
 * the Tango API, so we don't need to store them locally (and risk someone abusing the data).
 */
@Data
public class GiftLog {
    private final String orderId;
    private final Date date;
    private final String sessionName;
}

