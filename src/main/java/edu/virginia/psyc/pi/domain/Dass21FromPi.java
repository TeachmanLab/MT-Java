package edu.virginia.psyc.pi.domain;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_AS;
import lombok.Data;

import java.io.IOException;

/**
 * For converting the data sent by Andy at Pi, which is a JSON object, inside a string form parameter called "Data",
 * because he wasn't able to post raw Json data for some reason.
 */
@Data
public class Dass21FromPi {
    private String data;

    public DASS21_AS asDass21Object() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DASS21_AS dass21 = mapper.readValue(data, DASS21_AS.class);
        return dass21;
    }
}
