package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.tango.Account;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Used to award a gift certificates to eindividuals
 */
@Data
@Service
public class TangoGiftService {

    private static final String EMAIL_SUBJECT="Project Implicit Mental Health - Gift Certificate";
    private static final String EMAIL_MESSAGE="Thank you for your participation in our Study, please follow the directions below to receive your gift.";
    private static final String TANGO_SKU="TNGO-E-V-STD";


    @Value("${tango.id}")
    private String id;

    @Value("${tango.key}")
    private String key;

    @Value("${tango.url}")
    private String url;

    @Value("${email.respondTo}")
    private String from;

    @Value("${tango.accountId}")
    private String accountId;

    /**
     * HTTP Basic Authentication is required to connect to Tango.
     * This builds the HTTP headers to send along with the request.
     * @return
     */
    private HttpHeaders creds() {
        String plainCreds = id + ":" + key;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

    public void createAccount() {

    }

    public Account getAccountInfo() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(creds());
        URI uri = URI.create(url + "/accounts/" + id + "/" + accountId);
        ResponseEntity<Account> response = restTemplate.exchange(uri, HttpMethod.GET, request, Account.class);
        Account account = response.getBody();
        return account;
    }


}
