package org.mindtrails.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestQuestionnaire;
import org.mindtrails.MockClasses.TestQuestionnaireRepository;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.controller.ExportController;
import org.mindtrails.controller.QuestionController;
import org.mindtrails.controllers.BaseControllerTest;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.importData.ImportError;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.mindtrails.domain.tracking.ImportLog;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@AutoConfigureWebClient(registerRestTemplate=true)
@Transactional
public class ImportServiceTest extends BaseControllerTest {

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

    @Autowired
    JsPsychRepository jsPsychRepository;

    @Autowired
    TestQuestionnaireRepository testQuestionnaireRepository;

    @Autowired
    ParticipantExportRepository participantExportRepository;

    @Autowired
    StudyExportRepository studyExportRepository;

    @Autowired
    ImportLogRepository importLogRepository;

    @PersistenceContext
    private EntityManager em;


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

    @Test
    public void testFetchScaleUsesLastSuccessfulImportDate() {

        String response = "[{\"name\":\"simpleQuestions\", \"size\":20, \"deleteable\": true}]";

        Date startDate = new Date();

        // Make it appear that a successful request of simpleQuestions has occured before.
        ImportLog log = new ImportLog("simpleQuestions");
        log.setSuccessful(true);
        log.setDateStarted(startDate);
        importLogRepository.saveAndFlush(log);

        // The date we should request from the export service should be an hour earlier.
        DateFormat formater = new SimpleDateFormat(ExportService.DATE_FORMAT);
        Date offsetDate = new DateTime(startDate).minus(Hours.hours(1)).toDate();


        this.server.expect(requestTo(this.url + "/api/export/simpleQuestions?after=" +
                                    formater.format(offsetDate)))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
        this.importService.fetchScale("simpleQuestions");

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
        StudyImportExport study = new StudyImportExport();
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


    private Study createTestStudyViaImport(long studyId) throws Exception {
        StudyImportExport study = new StudyImportExport();
        study.setStudyType("Test Study");
        study.setId(studyId);
        study.setConditioning("Testing");
        study.setCurrentTaskIndex("0");

        importService.setMode("import");
        importService.importScale("study", IOUtils.toInputStream(
                objectMapper.writeValueAsString(study)
        ));
        return(studyRepository.findOne(studyId));
    }

    private Participant createTestParticipantViaImport(long participantId, long studyId) throws Exception {
        // Bit of hackery here to construct the relationship at export ime.
        TestStudy testStudy = new TestStudy();
        testStudy.setId(studyId);

        ParticipantExport testP = new ParticipantExport();
        testP.setId(participantId);
        testP.setStudy(testStudy);
        testP.setTheme("blue");
        testP.setOver18(true);

        importService.setMode("import");
        importService.importScale("participant", IOUtils.toInputStream(
                objectMapper.writeValueAsString(testP)
        ));
        return(participantRepository.findOne(participantId));
    }

    /**
     * See if you can import participant and study tables and then link them back together.
     *
     * @throws Exception
     */

    @Test
    public void testPSInfoCorrect() throws Exception {

        long participantId = 21L;
        long studyId = 1234L;
        createTestStudyViaImport(studyId);
        createTestParticipantViaImport(participantId, studyId);

        Participant p = participantRepository.findOne(participantId);
        assertNotNull(p);
        assertNotNull(p.getStudy());
        assertEquals("Test Study", p.getStudy().getName());
        assertEquals("Testing", p.getStudy().getConditioning());
    }

    /**
     *  Test if you can import a single task log entry if everything else is in place
     */
    @Test
    public void testTaskLog() throws Exception {
        long studyId = 1234L;
        createTestStudyViaImport(studyId);

        // Bit of hackery here to construct the relationship at export ime.
        TestStudy testStudy = new TestStudy();
        testStudy.setId(studyId);

        TaskLog log = new TaskLog(testStudy, 25.0, null, "testing");
        this.importService.setMode("import");
        assertEquals(0, this.taskLogRepository.findAll().size());
        importService.importScale("taskLog",IOUtils.toInputStream(this.objectMapper.writeValueAsString(log)));
        assertEquals(1, this.taskLogRepository.findAll().size());
        log = this.taskLogRepository.findAll().get(0);
        assertNotNull("Study should be correctly connected to the log.", log.getStudy());
        Study study = studyRepository.findById(studyId);
        assertEquals("The study should be exactly the one specified.", study, log.getStudy());
    }

    @Test
    public void testJsPsy() throws Exception {
        long participantId = 21L;
        long studyId = 1234L;
        long trialId = 51231234134L;
        Participant p = createTestParticipantViaImport(participantId, studyId);

        JsPsychTrial  trial = new JsPsychTrial(p,"Mobile",true);
        trial.setDate(new Date());
        trial.setId(trialId);
        assertEquals(0, this.jsPsychRepository.findAll().size());
        importService.importScale("jsPsychTrial",IOUtils.toInputStream(this.objectMapper.writeValueAsString(trial)));
        assertEquals(1, this.jsPsychRepository.findAll().size());
        JsPsychTrial savedTrial = this.jsPsychRepository.findAll().get(0);
        assertEquals("Mobile", savedTrial.getDevice());
        assertEquals(true, savedTrial.isCorrect());
        assertTrue(participantId == savedTrial.getParticipant().getId());
    }



    @Test
    public void testQuestionnaire() throws Exception {
        long participantId = 21L;
        long studyId = 1234L;
        long questionId = 88l;
        Participant p = createTestParticipantViaImport(participantId, studyId);

        TestQuestionnaire q = new TestQuestionnaire("someValue", p);
        q.setId(questionId);
        List<String> values = new ArrayList<>();
        values.add("ThingOne");
        values.add("ThingTwo");
        q.setMultiValue(values);

        assertEquals(0, testQuestionnaireRepository.findAll().size());

        int originalLogCount = importLogRepository.findAll().size();

        importService.importScale("TestQuestionnaire",IOUtils.toInputStream(this.objectMapper.writeValueAsString(q)));
        assertEquals(1, testQuestionnaireRepository.findAll().size());
        TestQuestionnaire savedQ = testQuestionnaireRepository.findAll().get(0);
        assertEquals(values, savedQ.getMultiValue());
        assertEquals("someValue", savedQ.getValue());
        assertEquals(participantId, savedQ.getParticipant().getId());

        // Assure the logs are updated after import.
        assertEquals(originalLogCount + 1, importLogRepository.findAll().size());
        ImportLog log = importLogRepository.findFirstByScaleAndSuccessfulOrderByDateStartedDesc("TestQuestionnaire", true);
        assertEquals("TestQuestionnaire", log.getScale());
        assertEquals(1, log.getSuccessCount());
        assertEquals(0, log.getErrorCount());
        assertTrue(log.isSuccessful());
        assertNotNull(log.getDateStarted());
    }



    @Test
    public void testSaveUpdatedParticipant() throws Exception {
        long participantId = 21L;
        long studyId = 1234L;
        Participant p = createTestParticipantViaImport(participantId, studyId);
        ParticipantExport pe = participantExportRepository.findOne(p.getId());
        pe.setTheme("pitiful yellow");

        importService.importScale("participant",IOUtils.toInputStream(this.objectMapper.writeValueAsString(pe)));

        Participant updatedP = participantRepository.findOne(participantId);
        assertEquals("pitiful yellow", updatedP.getTheme());
    }

    @Test
    public void testSaveUpdatedStudy() throws Exception {
        long studyId = 1234L;
        Study s = createTestStudyViaImport(studyId);

        StudyImportExport se = studyExportRepository.findOne(studyId);
        se.setConditioning("delightAndInspire");
        se.setCurrentTaskIndex("42");
        se.setCurrentSession("PostSession");

        importService.importScale("study",IOUtils.toInputStream(this.objectMapper.writeValueAsString(se)));

        em.clear(); // We have to fully flush the context in order to find the right records immeidately.
        Study updatedS = studyRepository.findOne(studyId);
        assertEquals("delightAndInspire", updatedS.getConditioning());
        assertEquals(42, updatedS.getCurrentTaskIndex());
        assertEquals("PostSession", updatedS.getCurrentSession().getName());
    }


    @Test
    public void testDateDrivenBackup() throws Exception {

    }


    @Test
    public void testLoadFromFile() throws Exception {

    }

    @Test
    public void testValidation() throws Exception {

    }

    @Test
    public void testDeleteCallIsMade() throws Exception {

    }

    @Test
    public void testDeleteIsDisabled() throws Exception {

    }


}