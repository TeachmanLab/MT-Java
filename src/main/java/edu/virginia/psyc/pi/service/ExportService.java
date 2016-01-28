package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.domain.DoNotDelete;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.QuestionnaireInfo;
import edu.virginia.psyc.pi.persistence.ExportLogDAO;
import edu.virginia.psyc.pi.persistence.ExportLogRepository;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.Questionnaire.QuestionnaireData;
import edu.virginia.psyc.pi.persistence.TrialDAO;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
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

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Keeps track of when the export service was last used, and how many records exist in the database.
 * Used by controllers if we need to disable log-ins to the site because the data is not getting exported
 * from the system on a regular basis.
 */
@Service
public class ExportService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ExportService.class);

    @Value("${export.maxRecords}")
    private int maxRecords;

    @Value("${export.maxMinutes}")
    private int maxMinutes;

    @Autowired ExportLogRepository exportLogRepository;
    @Autowired EmailService emailService;

    Repositories repositories;


    public int getMaxRecords() {
        return maxRecords;
    }

    public int getMaxMinutes() {
        return maxMinutes;
    }

    /** Rather than autowire all the repositories, this class will
     * gather a list of all repositories and filter out the ones that
     * are annotated as ExportAndDelete or that extend question
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.debug("CONTEXT loaded:  " + event.getApplicationContext());
        repositories=new Repositories(event.getApplicationContext());
    }

    public boolean disableAdditionalFormSubmissions() {
        if(totalDeleteableRecords() > maxRecords) return true;
        return false;
    }

    public int minutesSinceLastExport() {
        DateTime now = new DateTime(System.currentTimeMillis());
        DateTime last = new DateTime(lastExport().getDate());
        return Minutes.minutesBetween(last.toLocalDate(), now.toLocalDate()).getMinutes();
    }

    public int totalDeleteableRecords() {
        int sum = 0;
        for(QuestionnaireInfo i : listRepositories()) {
            if(i.isDeleteable()) sum += i.getSize();
        }
        return sum;
    }

    private ExportLogDAO lastExport() {
       return exportLogRepository.findFirstByOrderByIdDesc();
    }


    /**
     * Returns a repository for a given name.  Makes some
     * assumptions about the class.  This class is pretty dreadful, returns null
     * if it can't find a repository by name.
     */
    public JpaRepository getRepositoryForName(String name) {
        LOG.info("Looking for a repository for " + name);
        Class<?> domainType = getDomainType(name);
        if (domainType != null)
            return (JpaRepository) repositories.getRepositoryFor(domainType);
        LOG.info("failed to find a repository for " + name);
        return null;
    }

    /**
     * Returns a list of all repositories
     */
    public List<QuestionnaireInfo> listRepositories() {
        List<QuestionnaireInfo> names = new ArrayList<>();
        if(repositories == null) return names;
        boolean deleteableFlag;

        for (  Class<?> domainType : repositories) {
            Class<?> repoClass=repositories.getRepositoryInformationFor(domainType).getRepositoryInterface();
            Object repository=repositories.getRepositoryFor(domainType);
            // If this is questionnaire data ...
            if(QuestionnaireData.class.isAssignableFrom(domainType) || domainType.equals(TrialDAO.class)) {
                deleteableFlag        = !domainType.isAnnotationPresent(DoNotDelete.class);
                JpaRepository rep = (JpaRepository)repository;
                QuestionnaireInfo info = new QuestionnaireInfo(domainType.getSimpleName(), rep.count(), deleteableFlag);
                names.add(info);
            }
        }
        return names;
    }


    /**
     * Returns a Class for a given name.  Makes some
     * assumptions about the class.  This method is pretty dreadful, returns null
     * if it can't find a repository by name.
     */
    public Class<?> getDomainType(String name) {
        if(repositories == null) return null;
        for (  Class<?> domainType : repositories) {
            if (domainType.getSimpleName().equals(name)) {
                return domainType;
            }
        }
        return null;
    }

    private String getAlertMessage(int minutes, int records) {
            return(
                "It has been " + minutes + " minutes since an export has occurred. " +
                "Please make sure the export server is running correctly.  There are currently " +
                records + " records awaiting export.  You will receive messages about this " +
                "problem every 2 hours for the first 24 hours, and every 4 hours thereafter.");
    }

    /** Every 30 minutes:
     * If it's been more than 30 minutes (but less than 2 hours) since the least eport, notify
     * the admin of this fact.
     */
    @Scheduled(cron = "0 0,30 * * * *")
    public void send30MinAlert() throws MessagingException {
        LOG.debug("Running 30 minute alert.");
        int minutesSinceLastExport = minutesSinceLastExport();
        int totalRecords = totalDeleteableRecords();
        if(totalRecords > 0 && minutesSinceLastExport > 30 && minutesSinceLastExport < 60) {
            emailService.sendExportAlertEmail(getAlertMessage(minutesSinceLastExport, totalRecords));
        }
    }

    /**
     * Every 2 hours
     *    if it's been more than 2 hours but less than 24 hours since last export,
     *    send alerts every 2 hours if no exports are occurring.
     */
    @Scheduled(cron = "0 0 */2 * * *")
    public void send2hrAlert() throws MessagingException {
        LOG.debug("Running 2hr alert.");
        int minutesSinceLastExport = minutesSinceLastExport();
        int totalRecords = totalDeleteableRecords();
        if(totalRecords > 0 && minutesSinceLastExport > 120 && minutesSinceLastExport < 1440) {
            emailService.sendExportAlertEmail(getAlertMessage(minutesSinceLastExport, totalRecords));
        }
    }

    /**
     * Every 4 hours
     *    a) If we have exceeded the maximum records, alert admin that site is disabled.
     *    b) If it's been more than 24 hours since the last export, alert the admin of this fact.
     */
    @Scheduled(cron = "0 0 */4 * * *")
    public void send4hrAlert() throws MessagingException {
        LOG.debug("Running 4 hour alert.");
        int minutesSinceLastExport = minutesSinceLastExport();
        int totalRecords = totalDeleteableRecords();
        if(disableAdditionalFormSubmissions()) {
            emailService.sendExportAlertEmail("The site is currently disabled.  Too many " +
                    "records exist, and they need to be exported." +
                    getAlertMessage(minutesSinceLastExport, totalRecords));
        } else if(totalRecords > 0 && minutesSinceLastExport > 1440) {
            emailService.sendExportAlertEmail(getAlertMessage(minutesSinceLastExport, totalRecords));
        }
    }


}
