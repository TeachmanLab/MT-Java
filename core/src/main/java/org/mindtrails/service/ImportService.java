package org.mindtrails.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.jni.Error;
import org.mindtrails.domain.importData.ImportError;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mindtrails.domain.importData.Scale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.net.URI;


/**
 * Used to award a gift certificates to participants.
 *
 * Documentation is available here: https://github.com/tangocarddev/RaaS
 * There is great tool for working directly with the API here: https://integration-www.tangocard.com/raas_api_console/
 * Settings are read in from the file here: resources/application.properties, just look for the section on Tango.
 *
 * If you need to add money to the account for testing, the fake credit card we have setup has a cid of 32733202
 *
 *
 */

//@Data
@Service
public class ImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportService.class);

    @Value("${import.username}")
    private String username;

    @Value("${import.password}")
    private String password;

    @Value("${import.url}")
    private String url;

    @Autowired ExportService exportService;


/**
 *  Class finder.
 * */



    /**
     * Setup the headers for authorization.
     *
     * */

    private HttpHeaders headers() {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

/**
 * Here is the function to get a complete list of scale from api/export.
 *
 * */
    public List<Scale> importList() {
        LOGGER.info("Get into the original methods");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(url + "api/export/");
        LOGGER.info("calling url:" + uri.toString());
        ResponseEntity<List<Scale>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<List<Scale>>() {
        });
        LOGGER.info("Get something?");
        try {
            List<Scale> response = responseEntity.getBody();
            return response;
        } catch (HttpClientErrorException e) { throw new ImportError(e);}
    }



    /**
     *  The function that can get data from the online api, according to the name you fill
     *  in.
     *
     * */
    public String getOnline(String scale) {
        LOGGER.info("Get into the getOnline function");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(url + "api/export/" + scale);
        LOGGER.info("calling url:" + uri.toString());
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<String>() {
        });
        LOGGER.info("Get some items?");
        try {
            String response = responseEntity.getBody();
            return response;
        } catch (HttpClientErrorException e) {
            throw new ImportError(e);
        }
    }

    /**
     * Get the type/class/object for a name. This is much more difficult that I thought.
     *
     * */

    public Class<?> getClass(String scale) {
        LOGGER.info("What happens here?");
        Class<?> clz = exportService.getDomainType(scale);
        if (clz != null) {
            LOGGER.info(clz.getName());
            return clz;
        }
        LOGGER.info("Did not find it.");
        return null;
    }

/**
 * parse the data you get into the database.
 *
 * */



}
