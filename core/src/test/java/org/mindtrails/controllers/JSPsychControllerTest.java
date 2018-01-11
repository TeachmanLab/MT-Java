package org.mindtrails.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mindtrails.controller.JSPsychController;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.mindtrails.persistence.JsPsychRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JSPsychControllerTest extends BaseControllerTest {


    @Autowired
    protected JsPsychRepository jsPsychRepository;

    @Autowired
    private JSPsychController controller;

    @Override
    public Object[] getControllers() {
        return (new Object[]{controller});
    }

    private static final String EXAMPLE_DATA = "[\n" +
            " {\n" +
            "  \"rt\": 3012,\n" +
            "  \"stimulus\": \"introduction\",\n" +
            "  \"button_pressed\": 0,\n" +
            "  \"trial_type\": \"button-response\",\n" +
            "  \"trial_index\": 0,\n" +
            "  \"time_elapsed\": 3020,\n" +
            "  \"internal_node_id\": \"0.0-0.0\"\n" +
            " },\n" +
            " {\n" +
            "  \"rt\": 7587,\n" +
            "  \"stimulus\": \"You are in the grocery store and need to buy cereal. The store no longer has your favorite brand. As a result, you pick a new food for\",\n" +
            "  \"trial_type\": \"sentence-reveal\",\n" +
            "  \"trial_index\": 1,\n" +
            "  \"time_elapsed\": 7519,\n" +
            "  \"internal_node_id\": \"0.0-1.0\"\n" +
            " },\n" +
            " {\n" +
            "  \"rt\": 11689,\n" +
            "  \"stimulus\": \"breakfast\",\n" +
            "  \"button_pressed\": \"a,k\",\n" +
            "  \"correct\": true,\n" +
            "  \"trial_type\": \"missing-letters\",\n" +
            "  \"trial_index\": 2,\n" +
            "  \"time_elapsed\": 12121,\n" +
            "  \"internal_node_id\": \"0.0-2.0\"\n" +
            " },\n" +
            " {\n" +
            "  \"rt\": 8887,\n" +
            "  \"stimulus\": \"Do you buy something different to eat for breakfast?\",\n" +
            "  \"button_pressed\": \"Yes\",\n" +
            "  \"correct\": false,\n" +
            "  \"trial_type\": \"button-response-correct\",\n" +
            "  \"trial_index\": 3,\n" +
            "  \"time_elapsed\": 22010,\n" +
            "  \"internal_node_id\": \"0.0-3.0\"\n" +
            " },\n" +
            " {\n" +
            "  \"rt\": 2114,\n" +
            "  \"stimulus\": \"vividness\",\n" +
            "  \"button_pressed\": 2,\n" +
            "  \"trial_type\": \"button-response\",\n" +
            "  \"trial_index\": 4,\n" +
            "  \"time_elapsed\": 25128,\n" +
            "  \"internal_node_id\": \"0.0-4.0\"\n" +
            " },\n" +
            " {\n" +
            "  \"rt\": 1684,\n" +
            "  \"stimulus\": \"Use Imagination\",\n" +
            "  \"button_pressed\": 0,\n" +
            "  \"trial_type\": \"button-response\",\n" +
            "  \"trial_index\": 5,\n" +
            "  \"time_elapsed\": 27814,\n" +
            "  \"internal_node_id\": \"0.0-5.0\"\n" +
            " },\n" +
            " {\n" +
            "  \"rt\": 1099,\n" +
            "  \"stimulus\": \"vividness_final\",\n" +
            "  \"button_pressed\": 4,\n" +
            "  \"trial_type\": \"button-response\",\n" +
            "  \"trial_index\": 6,\n" +
            "  \"time_elapsed\": 29916,\n" +
            "  \"internal_node_id\": \"0.0-6.0\"\n" +
            " },\n" +
            " {\n" +
            "  \"rt\": 2764,\n" +
            "  \"stimulus\": \"final score screen\",\n" +
            "  \"button_pressed\": 0,\n" +
            "  \"trial_type\": \"button-response\",\n" +
            "  \"trial_index\": 7,\n" +
            "  \"time_elapsed\": 33681,\n" +
            "  \"internal_node_id\": \"0.0-7.0\"\n" +
            " }\n" +
            "]";

    public void postJSPsychData() throws Exception {
        ResultActions result = mockMvc.perform(post("/jspsych")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .content(EXAMPLE_DATA))
                .andExpect((status().isCreated()));
        List<JsPsychTrial> trials = jsPsychRepository.findAllByParticipantIdAndStudyAndSession(participant.getId(),
                participant.getStudy().getName(),
                participant.getStudy().getCurrentSessionModel().getName());
        assertNotNull(trials);
        assertEquals(8, trials.size());
    }

    @Test
    public void testGetJSPsychDataStatus() throws Exception {
        MvcResult result = mockMvc.perform(post("/jspsych/status")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .content(EXAMPLE_DATA))
                .andExpect((status().isOk()))
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(result.getResponse().getContentAsString());
        assertNotNull(actualObj);
        assertEquals(0, actualObj.size());

        // Send in some data, and make sure we get it back.
        postJSPsychData();
        result = mockMvc.perform(post("/jspsych/status")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .content(EXAMPLE_DATA))
                .andExpect((status().isOk()))
                .andReturn();
        actualObj = mapper.readTree(result.getResponse().getContentAsString());
        assertNotNull(actualObj);
        assertEquals(8, actualObj.size());

    }



}