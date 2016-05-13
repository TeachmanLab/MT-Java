package edu.virginia.psyc.mindtrails.domain.tango;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Created by dan on 7/17/15.
 */
public class TangoError extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(TangoError.class);

    public TangoError(HttpClientErrorException e) {
        super(e.getResponseBodyAsString());
        LOGGER.error("Tango API Error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString());
    }

}
