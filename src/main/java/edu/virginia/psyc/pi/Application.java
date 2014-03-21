package edu.virginia.psyc.pi;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * This is a Spring based application, and will auto discover the 
 * controllers in this code base and make them available
 *  as web endpoints.
 */
@ComponentScan  // Search for controllers and data access objects
@EnableAutoConfiguration  // Automatically configure everything ala Spring Boot
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}