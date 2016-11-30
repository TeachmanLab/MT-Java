package edu.virginia.psyc.r34;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * This is a Spring based application, and will auto discover the 
 * controllers in this code base and make them available
 *  as web endpoints.
 */
@ComponentScan ({"edu.virginia.psyc.r34", "org.mindtrails"})  // Search for controllers and data access objects in both Core and App projects.
@Configuration
@EnableJpaRepositories(basePackages = {"edu.virginia.psyc.r34", "org.mindtrails"}) // To find the core repositories.

@EnableAutoConfiguration  // Automatically configure everything ala Spring Boot
@EntityScan(basePackages = {"edu.virginia.psyc.r34", "org.mindtrails"}) // So we can find the other entities in the core controller.
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}