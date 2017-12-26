package org.mindtrails.service;

import org.mindtrails.domain.ImportError;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

@Data
@Service
public class ImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportService.class);

    @Value("import@gmail.com")
    private String username;

    @Value("ThisIs1Test!")
    private String password;

    @Value("http://localhost:9000/")
    private String url;


    public void setup(){
        url = "Http://localhost:9000/";
        username = "dihengz@gmail.com";
        password = "Il0veo8psy!";
    }


    private HttpHeaders headers() {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }


    public String importList() {
        LOGGER.info("Get into the original methods");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        setup();
        URI uri = URI.create(url + "api/export/");
        LOGGER.info("calling url:" + uri.toString());
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        LOGGER.info("Get something?");
        try {
            String response = responseEntity.getBody();
            return response;
        } catch (HttpClientErrorException e) { throw new ImportError(e);}
    }
}
