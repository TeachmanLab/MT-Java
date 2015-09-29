package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.domain.CBMStudy;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_AS;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_ASRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/24/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class QuestionControllerStaticTest {

    @Test
    public void testAppendObjectToCSV() throws Exception {

        DASS21_AS dass21;
        ArrayList<DASS21_AS> list;
        ParticipantDAO p;

        p = new ParticipantDAO("Dan", "daniel.h.funk@gmail.com", "passwd", true, "green");
        dass21 = new DASS21_AS(1,2,3,4,5,6,7);
        dass21.setParticipantDAO(p);
        dass21.setSession(CBMStudy.NAME.SESSION1.toString());
        list = new ArrayList<DASS21_AS>();
        list.add(dass21);

        String result = QuestionController.objectListToCSV(list);

        assert result.contains("SESSION1");
        assert result.contains("DASS21_AS");
    }


}