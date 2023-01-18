package org.mindtrails.service;

import org.joda.time.DateTime;
import org.joda.time.Days;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

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

    @Autowired
    private Environment env;

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

    @Value("${tango.customerId}")
    private String customerId;

    @Value("${tango.utid}")
    private String utid;

    private Catalog catalog;

    private ExchangeRates exchangeRates;


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
        URI uri = URI.create(url + "/accounts/" + accountId);
        LOGGER.info("Calling url:" + uri.toString());
        ResponseEntity<Account> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, Account.class);
        try {
            Account response = responseEntity.getBody();
            return response;
        } catch (HttpClientErrorException e) { throw new TangoError(e); }
    }

    /**
     * Returns account info.
     */
    public Catalog getCatalog() {

        if(this.catalog != null) return catalog;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(url + "/catalogs");
        LOGGER.info("Calling url:" + uri.toString());
        ResponseEntity<Catalog> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, Catalog.class);
        try {
            Catalog catalog = responseEntity.getBody();
            this.catalog = catalog;
            return catalog;
        } catch (HttpClientErrorException e) { throw new TangoError(e); }
    }

    /**
     * Returns account info.
     */
    public ExchangeRates getExchangeRates() {

        if(this.exchangeRates != null && this.exchangeRates.populated() &&
            this.exchangeRates.lastModifedDate().after(
                DateTime.now().minus(Days.days(1)).toDate())) {
            return this.exchangeRates;
        }
        LOGGER.info("RE-Loading Exchange Rates.");
        LOGGER.info("setting path:"+env.getProperty("spring.config.location")+", logPath:"+env.getProperty("logging.file")+", db:"+env.getProperty("spring.datasource.url"));
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(url + "/exchangerates");
        ResponseEntity<ExchangeRates> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, ExchangeRates.class);
        this.exchangeRates = responseEntity.getBody();
        return this.exchangeRates;
    }

    /**
     * Returns order / gift info.
     */
    public OrderResponse getOrderInfo(String orderId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(url + "/orders/" + orderId);
        try {
            ResponseEntity<OrderResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, OrderResponse.class);
            OrderResponse response = responseEntity.getBody();
            return response;
        } catch (HttpClientErrorException e) { throw new TangoError(e); }
    }

    /**
     * Places an order with Tango.  Returns Gift Card details that we can later use
     * to notify Participant.
     */
    public OrderResponse awardGiftCard(GiftLog log) {
        Order order = new Order(accountId, customerId, log.getTangoItemId(), log.getAmount(), false);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = headers();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> entity = new HttpEntity<>(order, headers);
        URI uri = URI.create(url + "/orders");
        try {
            ResponseEntity<OrderResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, OrderResponse.class);
            OrderResponse response = responseEntity.getBody();
            log.markAwarded(response.getReferenceOrderID());
            giftLogRepository.save(log);
            return response;
        } catch (HttpClientErrorException e) {
            throw new TangoError(e);
        }
    }

    /**
     * Creates a gift log record, without checking to see if one exists yet.
     */
    public GiftLog createGiftLogUnsafe(Participant participant, String sessionName, int dollarAmount) {
        if(participant.getAwardCountryCode() == null) {
            participant.setAwardCountryCode("US");
        }
        Item item = this.catalog.findItemByCountryCode(participant.getAwardCountryCode());
        double convertedAmount = this.getExchangeRates().convertFromUSDollars(dollarAmount, item.getCurrencyCode());
        GiftLog log = new GiftLog(participant, sessionName, convertedAmount, dollarAmount, item);
        this.giftLogRepository.save(log);
        return log;
    }


    /**
     * Makes a record in the gift log table that we have promised someone
     * a gift card, but it isn't awarded yet.
     */
    public GiftLog prepareGift(Participant participant, Session session, int dollarAmount) {
        GiftLog log = giftLogRepository.findByParticipantAndSessionName(participant, session.getName());
        if(log == null) {
            log = createGiftLogUnsafe(participant, session.getName(), dollarAmount);
        }
        return log;
    }

}
