package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.tango.Account;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Used to award a gift certificates to eindividuals
 */
@Data
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

    public Account getAccountInfo() {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = URI.create(url);
        return restTemplate.getForObject(uri, Account.class);
    }


}
