package org.mindtrails.domain.importData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Created by Diheng on 12/26/17.
 */
public class ImportError extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportError.class);

    public ImportError(HttpClientErrorException e) {
        super(e.getResponseBodyAsString());
        LOGGER.error("Import API Error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString());
    }

}

