package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.tango.Account;
import edu.virginia.psyc.pi.domain.tango.AccountResponse;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;

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
public class TangoGiftService {

    private static final String EMAIL_SUBJECT = "Project Implicit Mental Health - Gift Certificate";
    private static final String EMAIL_MESSAGE = "Thank you for your participation in our Study, please follow the directions below to receive your gift.";
    private static final String TANGO_SKU = "TNGO-E-V-STD";

    private static final Logger LOGGER = LoggerFactory.getLogger(TangoGiftService.class);

    @Value("${tango.id}")
    private String id;

    @Value("${tango.key}")
    private String key;

    @Value("${tango.url}")
    private String url;

    @Value("${email.respondTo}")
    private String from;

    @Value("${tango.accountEmail}")
    private String accountId;


    /**
     * HTTP Basic Authentication is required to connect to Tango.
     * This builds the HTTP headers to send along with the request.
     *
     * @return
     */
    private HttpHeaders headers() {
        String plainCreds = id + ":" + key;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        return headers;
    }

    /**
     * May not be necessary.
     * @return
     */
    public Account createAccount() {
        Account account = new Account();
        account.setIdentifier(this.id);
        account.setEmail(this.from);
        account.setCustomer(this.accountId);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headers();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
        URI uri = URI.create(url + "/accounts/");
        ResponseEntity<Account> response = restTemplate.exchange(uri, HttpMethod.POST, entity, Account.class);
        return response.getBody();
    }


    /**
     * Returns account info.  If the account does not exist, creates it.
     */
    public Account getAccountInfo() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(url + "/accounts/" + id + "/" + accountId);
        ResponseEntity<AccountResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, AccountResponse.class);
        AccountResponse response = responseEntity.getBody();
        return response.getAccount();
    }





}
