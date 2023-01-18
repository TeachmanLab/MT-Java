package edu.virginia.psyc.spanish;

import org.mindtrails.service.TangoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * This is a Spring based application, and will auto discover the 
 * controllers in this code base and make them available
 *  as web endpoints.
 *
 * If you are using this as a template, be sure to repace "org.mindtrails.basic" in the scan
 * paths below with the package or your own code base.
 */
@ComponentScan ({"edu.virginia.psyc.spanish", "org.mindtrails"})
@Configuration
@EnableJpaRepositories(basePackages = {"edu.virginia.psyc.spanish", "org.mindtrails"})
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EntityScan(basePackages = {"edu.virginia.psyc.spanish", "org.mindtrails"})
@EnableScheduling
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        //LOGGER.info("main:"+ new File("").getAbsolutePath());
        InputStream in = Application.class.getClassLoader().getResourceAsStream("default.properties");
        properties.load(in);

        File f = new File("");
        String appName = f.getAbsoluteFile().getName();
        String tomcatRoot = f.getAbsoluteFile().getParentFile().getAbsolutePath();
        String settingPath = Paths.get(tomcatRoot, "appConfig", appName+".properties").toAbsolutePath().toString();
        properties.setProperty("spring.config.name", appName);
        properties.setProperty("spring.config.location", "file:///"+settingPath);
        LOGGER.info("spring.config.location:"+"file:///"+settingPath);
//properties.setProperty("spring.config.additional-location", "file:///"+settingPath);
            //System.setProperty("spring.config.location", "file:./application.properties, optional:file:///"+settingPath);
            //InputStream in2 = new FileInputStream(f);
            //properties.load(in2);
        //}

        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(properties);
        app.run(args);
    }



}

