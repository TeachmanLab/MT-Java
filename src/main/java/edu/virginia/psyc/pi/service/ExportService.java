package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.persistence.Questionnaire.*;
import edu.virginia.psyc.pi.persistence.SensitiveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.repository.support.Repositories;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Automated process that extracts sensitive data from the database, writes that data out to
 * a text file, and sends the data to a remote server over ssh.    Once data is successfully
 * transfered and verified it is removed locally.
 *
 * It determines which repositories to do this to, based on an annotation applied to the
 * repository.
 */
@Service
@Component
public class ExportService implements  ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(ExportService.class);

    private Repositories repositories;
    private List<Class> updatedClasses = new ArrayList<>();

    /**
     * This should be called to hint to this Export Service that data is available
     * that is considered sensitive, and should be archived.
     * @return
     */
    public void recordUpdated(QuestionnaireData data) {
        updatedClasses.add(data.getClass());
    }
    public boolean exportNeeded() {
        return updatedClasses.size() > 0;
    }

    /**
     * Run on a very regular basis <5 minutes to check and see if an export
     * should be run.
     */
    @Scheduled(cron = "0 * * * * MON-FRI")
    public void exportIfThereAreChanges() {
        if (updatedClasses.size() == 0) return;
        for(Class c : updatedClasses) {
        }
    }


    /**
     * Run at startup to assure all sensitive data is exported.
     */
    @Scheduled(cron = "0 * * * * MON-FRI")
    public void exportAllSensitiveData() {
        if(repositories != null) {
            Iterator<Class<?>> list = repositories.iterator();
            while(list.hasNext()) {
                Class aClass = list.next();
                if (aClass.isAnnotationPresent(SensitiveData.class)) {

                }
            }
        } else {
            LOG.info("Not Ready");
        }
    }



    private void exportRecord(QuestionnaireData data) {
        LOG.info("Exporting Record : " + data);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        repositories = new Repositories(event.getApplicationContext());
    }

}
