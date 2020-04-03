package org.mindtrails.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestQuestionnaire;
import org.mindtrails.MockClasses.TestQuestionnaireRepository;
import org.mindtrails.MockClasses.TestUndeleteable;
import org.mindtrails.MockClasses.TestUndeleteableRepository;
import org.mindtrails.controller.ExportController;
import org.mindtrails.controller.QuestionController;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.RestExceptions.NotDeleteableException;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.service.ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dan on 10/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ExportControllerTest extends BaseControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ExportControllerTest.class);

    @Autowired
    private ExportController exportController;
    @Autowired
    private ImportService importService;
    @Autowired
    private QuestionController questionController;
    @Autowired
    private TestQuestionnaireRepository repo;
    @Autowired
    private TestUndeleteableRepository repoU;
    @Autowired
    ObjectMapper mapper;

    Date defaultDate = DateTime.now().minus(Days.days(2)).toDate();


    @Rule
    public ExpectedException thrown= ExpectedException.none();

    private TestQuestionnaire createTestEntry() {
        TestQuestionnaire q = new TestQuestionnaire();
        q.setDate(new Date());
        q.setValue("cheese");
        q.setMultiValue(Arrays.asList("cheddar", "havarti", "raw fungus"));
        q.setTimeOnPage(9.999);
        repo.save(q);
        repo.flush();
        return q;
    }

    @Before
    public void setModeToExport() {
        this.importService.setMode("export");
    }

    @Override
    public Object[] getControllers() {
        return (new Object[]{exportController, questionController});
    }


    @Test
    public void testScaleListContainsExpectedElements() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/export")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
        Scale[] scales = mapper.readValue(result.getResponse().getContentAsString(),
                Scale[].class);

        assertNotNull(this.findExportByName(scales,"studyImportExport"));
        assertNotNull(this.findExportByName(scales,"participantExport"));
        assertNotNull(this.findExportByName(scales,"testQuestionnaire"));
    }

    private Scale findExportByName(Scale[] data, String name) {
        for(int i=0; i < data.length; i++) {
            if (data[i].getName().equalsIgnoreCase(name)) {
                return data[i];
            }
        }
        return null;
    }


    @Test
    public void testEntryDataIsReturned() {
        createTestEntry();
        List data = exportController.listData("TestQuestionnaire", defaultDate);
        assertThat((List<Object>)data, hasItem(hasProperty("value", is("MyTestValue"))));
    }


    @Test
    public void testEntryDataCanBeDeleted() {
        // There should be at least one entry.
        TestQuestionnaire q = createTestEntry();
        LinkedQuestionnaireData qd;

        List data =  exportController.listData("TestQuestionnaire", defaultDate);
        assertThat(data.size(), greaterThan(0));

        // Going to make the assumption this is QuestionnaireData
        for(Object o : data) {
            qd = (LinkedQuestionnaireData)o;
            assertThat(qd.getId(), notNullValue());
            exportController.delete("TestQuestionnaire",qd.getId());
        }

        data =  exportController.listData("TestQuestionnaire", defaultDate);
        assertThat(data.size(), is(0));
    }

    @Test
    public void testDeleteIsNotPossibleInImportMode() throws Exception {
        importService.setMode("import");
        TestQuestionnaire q = createTestEntry();
        mockMvc.perform(get("/api/export/TestQuestionnaire/" + q.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is4xxClientError()));
    }


    @Test
    public void testSomeDataCannotBeDeleted() {
        // There should be at least one entry.
        TestUndeleteable u = new TestUndeleteable();
        u.setDate(new Date());
        u.setValue("MyTestValue");
        repoU.save(u);
        repoU.flush();

        LinkedQuestionnaireData qd;

        List data =  exportController.listData("TestUndeleteable", defaultDate);
        assertThat(data.size(), greaterThan(0));

        qd = (LinkedQuestionnaireData)data.get(0);
        assertThat(qd.getId(), notNullValue());

        assertTrue(TestUndeleteable.class.isAnnotationPresent(DoNotDelete.class));

        thrown.expect(NotDeleteableException.class);
        exportController.delete("TestUndeleteable",qd.getId());
    }


    @Test
    public void unknownFormsReturn404() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/export/ThisDoesNotExist")
                .with(user(admin)))
                .andExpect((status().is4xxClientError()))
                .andReturn();

    }

    /** Make sure that when the data is exported, the values are correctly encoded
     * @throws Exception
     */
    @Test
    public void testThatPostedDataIsExportedAsJSon() throws Exception {
        TestQuestionnaire testEntry = createTestEntry();
        MvcResult result = mockMvc.perform(get("/api/export/TestQuestionnaire")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
        JsonNode actualObj = mapper.readTree(result.getResponse().getContentAsString());
        JsonNode node = findNodeById(actualObj, testEntry.getId());
        assertEquals("Value is a top level object that contains the string 'cheese'", "cheese", node.path("value").asText());
        System.out.println(actualObj);
    }

    @Test
    public void testMultiValueElementsCorrectlyPassedThrough() throws Exception {
        TestQuestionnaire testEntry = createTestEntry();
        MvcResult result = mockMvc.perform(get("/api/export/TestQuestionnaire")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();

        JsonNode actualObj = mapper.readTree(result.getResponse().getContentAsString());
        JsonNode node = findNodeById(actualObj, testEntry.getId());

        Assert.assertThat(node.findValue("multiValue").isArray(), is(true));
        Iterator<JsonNode> values = node.findValue("multiValue").iterator();
        Assert.assertThat(values.next().textValue(), is("cheddar"));
        Assert.assertThat(values.next().textValue(), is("havarti"));
        System.out.println(actualObj);
    }

    @Test
    public void testParticipantDeIdentifiedInfoCorrect() throws Exception {
        participant.setTheme("chartreuse");
        participant.setOver18(true);
        participantRepository.save(participant);
        participantRepository.flush();
        MvcResult result = mockMvc.perform(get("/api/export/Participant")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
        JsonNode actualObj = mapper.readTree(result.getResponse().getContentAsString());
        JsonNode participantNode = findNodeById(actualObj, participant.getId());
        assertTrue("There should not be an email column in your json.", participantNode.path("email").isMissingNode());
        assertTrue(participantNode.path("over18").asBoolean());
        assertEquals("This should be chartreuse",participantNode.path("theme").asText(),"chartreuse");
        System.out.println(actualObj);
    }

    @Test
    public void testStudyInfoCorrect() throws Exception {
        s.setConditioning("Testing");
        studyRepository.save(s);
        studyRepository.flush();
        MvcResult result = mockMvc.perform(get("/api/export/Study")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
        JsonNode actualObj = mapper.readTree(result.getResponse().getContentAsString());
        JsonNode node = findNodeById(actualObj, s.getId());
        assertNotNull("We should find the study node.", node);
        assertEquals("This should be testing:","Testing",node.path("conditioning").asText());
        assertEquals("This should be 1:",1,actualObj.get(0).path("id").asInt());
    }

    private JsonNode findNodeById(JsonNode parent, Long id) {
        Iterator<JsonNode> nodes = parent.iterator();
        JsonNode node = null;
        while(nodes.hasNext()) {
            node = nodes.next();
            if(Long.parseLong(node.path("id").asText()) == id) {
                return node;
            }
        }
        return null;
    }

    @Test
    public void testConditionAssignment() throws Exception {
        s.setConditioning("testing");
        studyRepository.saveAndFlush(s);
        participantRepository.saveAndFlush(participant);

        String json = "{\"participantId\": " + participant.getId() + "," +
                "\"condition\": \"newCondition\"}";

        MvcResult result = mockMvc.perform(post("/api/export/condition")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();

        Participant pUpdated = participantRepository.findOne(participant.getId());

        assertEquals("newCondition", pUpdated.getStudy().getConditioning());

    }

    @Test
    public void testDateParameter() throws Exception {
        TestUndeleteable u1 = new TestUndeleteable();
        TestUndeleteable u2 = new TestUndeleteable();

        u1.setDate(DateTime.now().minus(Days.days(2)).toDate());
        u2.setDate(new Date());

        u1.setValue("My Old data record");
        u1.setValue("My new data record");

        repoU.save(u1);
        repoU.save(u2);
        repoU.flush();

        LinkedQuestionnaireData qd;


        List data =  exportController.listData("TestUndeleteable",
                DateTime.now().minus(Hours.hours(1)).toDate());
        assertEquals(1, data.size());

        data =  exportController.listData("TestUndeleteable",
                DateTime.now().minus(Days.days(10)).toDate());
        assertEquals(2, data.size());

    }


}
