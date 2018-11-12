package org.mindtrails.service;

import org.mindtrails.domain.Session;
import org.mindtrails.domain.tango.*;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.GiftLogRepository;
import org.mindtrails.persistence.ParticipantRepository;
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
public class TangoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TangoService.class);

    @Value("${tango.enabled}")
    private Boolean enabled;

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

    @Value("${tango.customer}")
    private String customer;

    @Value("${tango.tangoCardSku}")
    private String tangoCardSku;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private GiftLogRepository giftLogRepository;

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
        account.setCustomer(this.customer);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headers();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
        URI uri = URI.create(url + "/accounts/");
        try {
            ResponseEntity<Account> response = restTemplate.exchange(uri, HttpMethod.POST, entity, Account.class);
            return response.getBody();
        } catch (HttpClientErrorException e) { throw new TangoError(e); }
    }

    /**
     * Just in case you need to fund the test account.  Good starting point if we later decide to allow the system
     * to make payments from a credit card.
     *
     * @return
     */
    public void fundTestAccount() {
        FundAccount fundAccount = new FundAccount(this.id, this.accountId, 1000, "192.168.1.1", "123", "32733202");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headers();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FundAccount> entity = new HttpEntity<>(fundAccount, headers);
        URI uri = URI.create(url + "/cc_fund");
        try {
            restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) { throw new TangoError(e); }
    }


    /**
     * Returns account info.
     */
    public Account getAccountInfo() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(url + "/accounts/" + customer + "/" + accountId);
        LOGGER.info("Calling url:" + uri.toString());
        ResponseEntity<AccountResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, AccountResponse.class);
        try {
            AccountResponse response = responseEntity.getBody();
            return response.getAccount();
        } catch (HttpClientErrorException e) { throw new TangoError(e); }
    }

    /**
     * Returns order / gift info.
     */
    public Order getOrderInfo(String orderId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(url + "/orders/" + orderId);
        try {
            ResponseEntity<OrderResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, OrderResponse.class);
            OrderResponse response = responseEntity.getBody();
            return response.getOrder();
        } catch (HttpClientErrorException e) { throw new TangoError(e); }
    }

    /**
     * Places an order with Tango.  Returns Gift Card details that we can later use
     * to notify Participant.
     */
    public Reward awardGiftCard(GiftLog log) {
        Participant participant = log.getParticipant();
        Recipient recipient = new Recipient(participant.getFullName(), participant.getEmail());
        Order order = new Order(customer, accountId, tangoCardSku, log.getAmount(), false);
        order.setRecipient(recipient);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headers();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> entity = new HttpEntity<>(order, headers);
        URI uri = URI.create(url + "/orders");
        try {
            ResponseEntity<OrderResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, OrderResponse.class);
            Reward r = response.getBody().getOrder().getReward();
            r.setOrder_id(response.getBody().getOrder().getOrder_id());
            log.markAwarded(r.getOrder_id());
            giftLogRepository.save(log);
            return response.getBody().getOrder().getReward();
        } catch (HttpClientErrorException e) {
            throw new TangoError(e);
        }
    }

    /**
     * Makes a record in the gift log table that we have promised someone
     * a gift card, but it isn't awarded yet.
     */
    public void prepareGift(Participant participant, Session session, int amount) {
        GiftLog logDAO = giftLogRepository.findByParticipantAndSessionName(participant, session.getName());
        if(logDAO != null) {
            logDAO = new GiftLog(participant, session.getName(), amount);
            this.giftLogRepository.save(logDAO);
        }
    }

}
