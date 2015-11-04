package edu.virginia.psyc.pi.domain;

import lombok.Data;
import org.apache.catalina.util.StringParser;

/**
 * Created by dan on 10/23/15.
 */
@Data
public class QuestionnaireInfo {
    private final String name;
    private final long   size;
    private final boolean deleteable;
}
