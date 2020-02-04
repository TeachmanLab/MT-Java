package org.mindtrails.controllers;

import org.junit.Test;
import org.mindtrails.controller.ActionController;
import org.mindtrails.domain.tracking.ActionLog.ActionLog;
import org.mindtrails.persistence.ActionLogRepository;
import org.mindtrails.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ActionControllerTest extends BaseControllerTest {


    @Autowired
    private ActionController actionController;

    @Autowired
    protected ActionLogRepository actionLogRepository;

    @Autowired
    private ImportService importService;
    

    public Object[] getControllers() {
        return (new Object[]{actionController});
    }

    private static final String EXAMPLE_DATA = "{\n" +
            "\"name\":\"SampleActionName\",\n" +
            "\"latency\":50000\n" +
            "}\n";

    @Test
    public void postData() throws Exception {
        importService.setMode("export");
        List<ActionLog> preData = actionLogRepository.findAllByParticipantAndStudyNameOrderByDate(participant,
                participant.getStudy().getName());
        ResultActions result = mockMvc.perform(post("/action")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .content(EXAMPLE_DATA))
                .andExpect((status().isCreated()));
        List<ActionLog> data = actionLogRepository.findAllByParticipantAndStudyNameOrderByDate(participant,
                participant.getStudy().getName());
        assertNotNull(data);
        assertEquals(preData.size() + 1, data.size());
    }

    @Test
    public void testPostInImportMode() throws Exception {
        importService.setMode("import");
        ResultActions result = mockMvc.perform(post("/action")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .content(EXAMPLE_DATA))
                .andExpect((status().is4xxClientError()));
    }

}
