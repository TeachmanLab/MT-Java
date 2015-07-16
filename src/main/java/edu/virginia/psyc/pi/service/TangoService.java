package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.tango.*;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TangoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TangoService.class);

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

    @Value("${tango.tangoCardSku}")
    private String tangoCardSku;

    @Value("${tango.cardValueCents}")
    private int cardValueCents;

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
     *
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
        LOGGER.info("Calling url:" + uri.toString());
        ResponseEntity<AccountResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, AccountResponse.class);
        AccountResponse response = responseEntity.getBody();
        return response.getAccount();
    }

    /**
     * Places an order with Tango.  Returns Gift Card details that we can later use
     * to notify Participant.
     */
    public Reward createGiftCard(Participant participant) {
        Recipient recipient = new Recipient(participant.getFullName(), participant.getEmail());
        Order order = new Order(id, accountId, tangoCardSku, cardValueCents, false);
        order.setRecipient(recipient);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headers();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> entity = new HttpEntity<>(order, headers);
        URI uri = URI.create(url + "/orders");
        try {
            ResponseEntity<OrderResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, OrderResponse.class);
            return response.getBody().getOrder().getReward();
        } catch (HttpClientErrorException e) {
            LOGGER.info("Failed to create a gift card.");
            LOGGER.info("Response code is: " + e.getStatusCode());
            LOGGER.info("Response body is: " + e.getResponseBodyAsString());
            LOGGER.info("Error is : " + e.getMessage());
            throw e;
        }
    }


}
