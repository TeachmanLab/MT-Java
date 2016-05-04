package org.virginia.psyc.mindtrails;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/28/14
 * Time: 12:32 PM
 * Just a touch more wiring to get War files to work correctly when remotely deployed.
 */
public class PiWebXml extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
