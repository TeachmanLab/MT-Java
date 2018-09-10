package org.mindtrails.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.*;
import org.mindtrails.controller.ExportController;
import org.mindtrails.controller.QuestionController;
import org.mindtrails.controllers.BaseControllerTest;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.importData.Scale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.persistence.EmailLogRepository;
import org.mindtrails.persistence.JsPsychRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static junit.framework.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.InputStream;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/22/14
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Transactional
public class ImportServiceTest extends BaseControllerTest {

    @Autowired
    private ImportService service;
    private String testScale = "JsPsychTrial";

    @Autowired
    private ExportController exportController;
    @Autowired
    private QuestionController questionController;
    @Autowired
    private TestQuestionnaireRepository repo;
    @Autowired
    private TestUndeleteableRepository repoU;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EmailLogRepository emailRepo;
    @Autowired
    private JsPsychRepository jsPsyRepo;


    private static final Logger LOGGER = LoggerFactory.getLogger((ImportServiceTest.class));


    private void createTestEntries() {
        Participant p = new Participant("Joe Test", "j@t.com", false);
        p.setStudy(new TestStudy());
        entityManager.persist(p);
        entityManager.persist(new TestQuestionnaire("MyTestValue",p));
        entityManager.persist(new TestUndeleteable("MyTestValue"));
        entityManager.persist(new GiftLog(p, "Order1", "Session 1"));
        entityManager.persist(new EmailLog(p, "Email1"));
        entityManager.persist(new TaskLog(p.getStudy(), 25.0));
        entityManager.persist(new JsPsychTrial(1l,"Mobile",true));
    }

    @Before
    public void setMode() {
        this.service.setMode("import");
    }

    @Override
    public Object[] getControllers() {
        return (new Object[]{exportController, questionController});
    }


    /**
     * Testing for @ImportMode and @ExportMode
     * @throws Exception
     */




/*    @Test
    public void saveAllLog() throws Exception {
        LOGGER.info("Try to update all the log files");
        int i = 0;
        List<String> good = new ArrayList<String>();
        List<String> bad = new ArrayList<String>();
        List<Scale> list = service.fetchListOfScales();
        for (Scale scale : list) {
            if (scale.getName().toLowerCase().contains("log")) {
                String is = service.fetchScale("",scale.getName());
                if (service.importScale(scale.getName(), is)) {
                    i += 1;
                    good.add(scale.getName());
                } else {
                    bad.add(scale.getName());
                }
            }
        }
        LOGGER.info("Here is the good list:");
        for (String flag : good) LOGGER.info(flag);
        LOGGER.info("Here is the bad list:");
        for (String flag : bad) LOGGER.info(flag);
        assertEquals(i, list.size());
    }*/


/*    @Test
    public void saveAllScale() throws Exception {
        LOGGER.info("Save All Scale!!!!");
        int i = 0;
        List<String> good = new ArrayList<String>();
        List<String> bad = new ArrayList<String>();
        List<Scale> list = service.fetchListOfScales();
        for (Scale scale : list) {
            String is = service.fetchScale("",scale.getName());
            if (service.importScale(scale.getName(), is)) {
                i += 1;
                good.add(scale.getName());
            } else {
                bad.add(scale.getName());
            }
        }
        LOGGER.info("Here is the good list:");
        for (String flag : good) LOGGER.info(flag);
        LOGGER.info("Here is the bad list:");
        for (String flag : bad) LOGGER.info(flag);
        assertEquals(i, list.size());
    }*/






    /**
     *  Test if you can import the task_log and link it to the study object.
     */


    /**
     * Test if you can import the questionnaire and logs beside task_log.
     */

    @Test
    public void testGeneral() throws Exception {
        createTestEntries();
        MvcResult result = mockMvc.perform(get("/api/export/TestQuestionnaire")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode questObj = mapper.readTree(result.getResponse().getContentAsString());

        repo.delete((long) 1);
        service.importScale("TestQuestionnaire", IOUtils.toInputStream(questObj.toString()));

        MvcResult resultLog = mockMvc.perform(get("/api/export/EmailLog")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
        JsonNode logObj = mapper.readTree(resultLog.getResponse().getContentAsString());

        emailRepo.deleteAll();
        service.importScale("EmailLog", IOUtils.toInputStream(logObj.toString()));
    }

    /**
     *  Test if you can get a list of files locally.
     * @throws Exception
     */
    @Test
    public void testGetFileList() throws Exception {
        String testScale = "WhatIBelieve";
        File[] list = service.getFileList(testScale);
        for (File file:list) {
            System.out.println(file.getName());
        }
    }



    /**
     * This is a test for the missing data log.
     */

    @Test
    public void testMissingLog() throws Exception {
        String tScale ="WhatIBelieve";
        ObjectMapper mapper = new ObjectMapper();
        String is = "[{\"id\": 1, \"date\": \"Sun, 30 Apr 2017 13:49:40 -0500\", \"session\": \"preTest\", \"tag\": null, \"timeOnPage\": null, \"difficultTasks\": 2, \"performEffectively\": 0, \"compared\": 3, \"learn\": 3, \"particularThinking\": 0, \"alwaysChangeThinking\": 4, \"wrongWill\": 2, \"hardlyEver\": 2, \"participant\": \"1\"}]";
        JsonNode obj = mapper.readTree(is);
        assertTrue(service.saveMissingLog(obj.elements().next(),tScale));

    }

    /**
     *  Test if you can import the JsPsych data
     */
    @Test
    public void testJsPsy() throws Exception {
        createTestEntries();
        MvcResult result = mockMvc.perform(get("/api/export/JsPsychTrial")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode questObj = mapper.readTree(result.getResponse().getContentAsString());
        jsPsyRepo.flush();
        jsPsyRepo.deleteAll();
        service.importScale("JsPsychTrial", IOUtils.toInputStream(questObj.toString()));
        jsPsyRepo.flush();
        System.out.println(questObj.toString());
    }

}


