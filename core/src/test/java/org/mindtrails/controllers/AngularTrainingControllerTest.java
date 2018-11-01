package org.mindtrails.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mindtrails.controller.AngularTrainingController;
import org.mindtrails.controller.JSPsychController;
import org.mindtrails.domain.AngularTraining.AngularTraining;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.mindtrails.persistence.AngularTrainingRepository;
import org.mindtrails.persistence.JsPsychRepository;
import org.mindtrails.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AngularTrainingControllerTest extends BaseControllerTest {


    @Autowired
    protected AngularTrainingRepository angularTrainingRepository;

    @Autowired
    private AngularTrainingController angularTrainingController;

    @Autowired
    private ImportService importService;

    @Override
    public Object[] getControllers() {
        return (new Object[]{angularTrainingController});
    }

    private static final String EXAMPLE_DATA = "[\n" +
            "{\n" +
            "\"buttonPressed\":\"\",\n" +
            "\"conditioning\":\"Control\",\n" +
            "\"correct\":true,\n" +
            "\"device\":\"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36\",\n" +
            "\"rt\":5317.399999999907,\n" +
            "\"rtFirstReact\":5317.399999999907,\n" +
            "\"session\":\"introSession\",\n" +
            "\"sessionTitle\":\"Introduction to CalmThinking: undefined\",\n" +
            "\"stepIndex\":0,\n" +
            "\"stepTitle\":\"How Does Calm Thinking Work?\",\n" +
            "\"stimulus\":\"The Calm Thinking program is five sessions. You will complete one session a week, and each " +
            "session takes about 15 minutes or fewer. You will also answer some questions as part of our research study" +
            " so we can evaluate how well the program works.\",\n" +
            "\"study\":\"Calm Thinking\",\n" +
            "\"timeElapsed\":5355.999999970663,\n" +
            "\"trialType\":\"Paragraph\"\n" +
            "},\n" +
            "{\n" +
            "\"buttonPressed\":\"Extremely Important\",\n" +
            "\"conditioning\":\"Control\",\n" +
            "\"correct\":true,\n" +
            "\"device\":\"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36\",\n" +
            "\"rt\":4083.900000026915,\n" +
            "\"rtFirstReact\":1662.2000000206754,\n" +
            "\"session\":\"introSession\",\n" +
            "\"sessionTitle\":\"Introduction to CalmThinking: undefined\",\n" +
            "\"stepIndex\":0,\n" +
            "\"stepTitle\":\"How Does Calm Thinking Work?\",\n" +
            "\"stimulus\":\"How important is reducing your anxiety to you right now?\",\n" +
            "\"study\":\"Calm Thinking\",\n" +
            "\"timeElapsed\":109349.19999999693,\n" +
            "\"trialType\":\"Question\"\n" +
            "}\n" +
            "]\n";

    @Test
    public void postData() throws Exception {
        importService.setMode("export");
        List<AngularTraining> preData = angularTrainingRepository.findAllByParticipantAndSessionOrderByDate(participant,
                participant.getStudy().getCurrentSession().getName());
        ResultActions result = mockMvc.perform(post("/angularTraining")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .content(EXAMPLE_DATA))
                .andExpect((status().isCreated()));
        List<AngularTraining> data = angularTrainingRepository.findAllByParticipantAndSessionOrderByDate(participant,
                participant.getStudy().getCurrentSession().getName());
        assertNotNull(data);
        assertEquals(preData.size() + 2, data.size());
    }

    @Test
    public void testPostInImportMode() throws Exception {
        importService.setMode("import");
        ResultActions result = mockMvc.perform(post("/angularTraining")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .content(EXAMPLE_DATA))
                .andExpect((status().is4xxClientError()));
    }


    @Test
    public void testGetLastAngularTrainingRecord() throws Exception {
        mockMvc.perform(get("/angularTraining/last")
                .with(SecurityMockMvcRequestPostProcessors.user(participant)))
                .andExpect((status().isNotFound()));

        // Send in some data, and make sure we get it back.
        postData();
        MvcResult result = mockMvc.perform(post("/angularTraining/last")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .content(EXAMPLE_DATA))
                .andExpect((status().isOk()))
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResult = mapper.readTree(result.getResponse().getContentAsString());
        assertNotNull(jsonResult);
        assertEquals("Extremely Important", jsonResult.get("buttonPressed").textValue());

    }
}