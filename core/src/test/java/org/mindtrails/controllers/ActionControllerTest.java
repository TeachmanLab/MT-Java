package org.mindtrails.controllers;

import org.junit.Test;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tracking.ActionLog;
import org.mindtrails.persistence.ActionLogRepository;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ImportService;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ActionControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ActionLogRepository actionLogRepository;

    @Autowired
    private ImportService importService;
    
    protected Participant participant;

    public Object[] getControllers() {
        return (new Object[]{actionLogRepository});
    }

    private static final String EXAMPLE_DATA = "{\n" +
            "\"actionName\":\"SampleActionName\"\n" +
            "}\n";

    @Test
    public void postData() throws Exception {
        importService.setMode("export");
        List<ActionLog> preData = actionLogRepository.findAllByParticipantAndStudyNameOrderByDateRecorded(participant,
                participant.getStudy().getName());
        ResultActions result = mockMvc.perform(post("/action")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .content(EXAMPLE_DATA))
                .andExpect((status().isCreated()));
        List<ActionLog> data = actionLogRepository.findAllByParticipantAndStudyNameOrderByDateRecorded(participant,
                participant.getStudy().getName());
        assertNotNull(data);
        assertEquals(preData.size() + 2, data.size());
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
