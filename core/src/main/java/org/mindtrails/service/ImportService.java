package org.mindtrails.service;

import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.questionnaire.QuestionnaireInfo;
import org.mindtrails.domain.tracking.ExportLog;
import org.mindtrails.persistence.ExportLogRepository;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Diheng, 12/04/2017
 *
 */

/**
 * This is the java version of the MTData. It includes a data reader (send request and read
 * data from front end server), a getter and a writer. It also includes the fixed schedules.
 * It also comes with a backup option, so that you can read all the json from local instead
 * of the endpoint.
 *
 */
@Service
public class ImportService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ImportService.class);



}