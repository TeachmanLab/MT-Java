package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.DAO.TestQuestionnaire;
import edu.virginia.psyc.pi.DAO.TestQuestionnaireRepository;
import edu.virginia.psyc.pi.DAO.TestUndeleteable;
import edu.virginia.psyc.pi.DAO.TestUndeleteableRepository;
import edu.virginia.psyc.pi.service.RestExceptions.NotDeleteableException;
import edu.virginia.psyc.pi.persistence.Questionnaire.SecureQuestionnaireData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;

/**
 * Created by dan on 10/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ExportControllerTest extends BaseControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ExportControllerTest.class);

    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private ExportController exportController;
    @Autowired
    private TestQuestionnaireRepository repo;
    @Autowired
    private TestUndeleteableRepository repoU;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    private MockMvc mockMvc;

    private void createTestEntry() {
        TestQuestionnaire q = new TestQuestionnaire();
        q.setDate(new Date());
        q.setValue("MyTestValue");
        repo.save(q);
        repo.flush();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(exportController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .addFilters(this.springSecurityFilterChain)
                .build();
    }

    @Test
    public void testEntryDataIsReturned() {
        createTestEntry();
        List data = exportController.listData("TestQuestionnaire",0);
        assertThat((List<Object>)data, hasItem(hasProperty("value", is("MyTestValue"))));
    }

    @Test
    public void testEntryDataCanBeDeleted() {
        // There should be at least one entry.
        createTestEntry();
        SecureQuestionnaireData qd;

        List data =  exportController.listData("TestQuestionnaire",0);
        assertThat(data.size(), greaterThan(0));

        // Going to make the assumption this is QuestionnaireData
        for(Object o : data) {
            qd = (SecureQuestionnaireData)o;
            assertThat(qd.getId(), notNullValue());
            exportController.delete("TestQuestionnaire",qd.getId());
        }

        data =  exportController.listData("TestQuestionnaire",0);
        assertThat(data.size(), is(0));
    }

    @Test
    public void testSomeDataCannotBeDeleted() {
        // There should be at least one entry.
        TestUndeleteable u = new TestUndeleteable();
        u.setDate(new Date());
        u.setValue("MyTestValue");
        repoU.save(u);
        repoU.flush();

        SecureQuestionnaireData qd;

        List data =  exportController.listData("TestUndeleteable",0);
        assertThat(data.size(), greaterThan(0));

        qd = (SecureQuestionnaireData)data.get(0);
        assertThat(qd.getId(), notNullValue());

        thrown.expect(NotDeleteableException.class);
        exportController.delete("TestUndeleteable",qd.getId());
    }

    @Test
    public void unknownFormsReturn404() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/export/ThisDoesNotExist")
                .with(user(getAdmin())))
                .andExpect((status().is4xxClientError()))
                .andReturn();

    }

}
