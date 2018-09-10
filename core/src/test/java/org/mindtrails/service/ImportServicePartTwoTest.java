package org.mindtrails.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.controller.ExportController;
import org.mindtrails.controller.QuestionController;
import org.mindtrails.controllers.BaseControllerTest;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.importData.ImportError;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.persistence.ParticipantExport;
import org.mindtrails.persistence.StudyExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@AutoConfigureWebClient(registerRestTemplate=true)
@Transactional
public class ImportServicePartTwoTest  extends BaseControllerTest {

    @Autowired
    private ExportController exportController;

    @Autowired
    private QuestionController questionController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ImportService importService;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer server;

    @Value("${import.url}")
    private String url;


    @Override
    public Object[] getControllers() {
        return (new Object[]{exportController, questionController});
    }


    @Before
    public void setUp() {
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        server = MockRestServiceServer.createServer(gateway);
    }


    @Test(expected = ImportError.class)
    public void callFunction() {
        this.server.expect(requestTo(this.url + "/api/export/nosuch/1"))
                .andRespond(withBadRequest());
         importService.deleteScaleItem("nosuch",1);
    }

    @Test
    public void getScaleList() {

        String response = "[{\"name\":\"simpleQuestions\", \"size\":20, \"deleteable\": true}]";

        this.server.expect(requestTo(this.url + "/api/export"))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
        List<Scale> list = this.importService.fetchListOfScales();
        assertEquals(1, list.size());
    }

    @Test
    public void get() {

        String response = "[{\"name\":\"simpleQuestions\", \"size\":20, \"deleteable\": true}]";

        this.server.expect(requestTo(this.url + "/api/export"))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
        List<Scale> list = this.importService.fetchListOfScales();
        assertEquals(1, list.size());
    }



    /**
     *  Test if you can import a single task log entry if everything else is in place.
     */
    @Test
    public void testTaskLog() throws Exception {
        TaskLog log = new TaskLog(participant.getStudy(), 25.0);
        this.importService.setMode("import");
        assertEquals(0, this.taskLogRepository.findAll().size());
        importService.importScale("taskLog",IOUtils.toInputStream(this.objectMapper.writeValueAsString(log)));
        assertEquals(1, this.taskLogRepository.findAll().size());
    }

    @Test
    public void testParticipantImport() throws Exception {

        ParticipantExport testP = new ParticipantExport();
        long testId = 21L;
        testP.setAdmin(false);
        testP.setId(testId);
        testP.setStudy(new TestStudy());
        testP.setTheme("blue");
        testP.setOver18(true);

        importService.setMode("import");
        importService.importScale("participant", IOUtils.toInputStream(objectMapper.writeValueAsString(testP)));
        Participant savedParticipant = participantRepository.findOne(testId);
        Assert.assertNotNull("The participant should be saved under the same id as it was before", savedParticipant);
    }


    /**
     * See if you can import the study table.
     */
    @Test
    public void testStudyImport() throws Exception {

        long testId = 1234L;
        StudyExport study = new StudyExport();
        study.setStudyType("Test Study");
        study.setId(testId);
        study.setConditioning("Testing");
        study.setCurrentSession("Peanuts");
        study.setCurrentTaskIndex("14");
        study.setLastSessionDate(new Date());
        study.setReceiveGiftCards(true);

        importService.setMode("import");
        importService.importScale("study", IOUtils.toInputStream(
                objectMapper.writeValueAsString(study)
        ));
        Study updatedStudy = studyRepository.findById(testId);
        Assert.assertNotNull(updatedStudy);
        Assert.assertEquals("Testing", updatedStudy.getConditioning());
    }




}