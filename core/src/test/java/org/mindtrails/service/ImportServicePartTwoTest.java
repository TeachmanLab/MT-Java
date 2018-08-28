package org.mindtrails.service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.domain.importData.Scale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration()
@RestClientTest(ImportService.class)
public class ImportServicePartTwoTest {

    @Autowired
    private ImportService importService;

    @Autowired
    private MockRestServiceServer server;

    @Test
    public void getScaleList()
            throws Exception {
        this.server.expect(requestTo("/greet/details"))
                .andRespond(withSuccess("hello", MediaType.TEXT_PLAIN));
        List<Scale> list = this.importService.importList("/greet/details");
        assertEquals(0, list.size());
    }

}