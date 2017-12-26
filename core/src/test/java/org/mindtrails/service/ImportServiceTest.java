package org.mindtrails.service;

import org.mindtrails.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.*;

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
public class ImportServiceTest {

    @Autowired
    private ImportService service;

    @Test
    public void getScaleList() throws Exception {
        String list = service.importList();
        assertNotNull(list);
    }

}
