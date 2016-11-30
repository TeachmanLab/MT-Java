package edu.virginia.psyc.r34.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.r34.persistence.Questionnaire.DASS21_AS;
import lombok.Data;

import java.io.IOException;

/**
 * For converting the data sent by Andy at Pi, which is a JSON object,
 * inside a string form parameter called "Data", because he wasn't able
 * to post raw Json data for some reason.  Also, the site can't do 0 based
 * forms, so we have to substract 1 from each value to get the correct score.
 */
@Data
public class Dass21FromPi {
    private String data;

    public DASS21_AS asDass21Object() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DASS21_AS dass21 = mapper.readValue(data, DASS21_AS.class).decrementBy1();
        return dass21;
    }
}
