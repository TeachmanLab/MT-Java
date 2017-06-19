package org.mindtrails.service;

import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Diheng on 6/19/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class ImportServiceTest {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ImportServiceTest.class);

    @Autowired
    private ImportService service;


    /**
     *  Create the testing data for import service; You can use the
     */
    private void createTestData(){


    }

    private void setJsonPath() {


    }


    private void testDataImportFromServer() {
        createTestData();
        service.

    }

    private void testDataImportFromJson() {
        setJsonPath();

    }



}
