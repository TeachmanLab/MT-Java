package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.DoNotDelete;
import edu.virginia.psyc.pi.domain.RestExceptions.NoSuchQuestionnaireException;
import edu.virginia.psyc.pi.domain.RestExceptions.NotDeleteableException;
import edu.virginia.psyc.pi.domain.QuestionnaireInfo;
import edu.virginia.psyc.pi.persistence.ExportLogDAO;
import edu.virginia.psyc.pi.persistence.ExportLogRepository;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.QuestionnaireData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a tool for Exporting data from the system and then
 * safely removing that data once it is on the remote system.
 *
 */
@Controller
@RequestMapping("/api/export")
public class ExportController implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ExportController.class);

    Repositories repositories;

    @Autowired ParticipantRepository participantRepository;
    @Autowired ExportLogRepository exportLogRepository;

    /** Rather than autowire all the repositories, this class will
     * gather a list of all repositories and filter out the ones that
     * are annotated as ExportAndDelete or that extend question
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.debug("CONTEXT loaded:  " + event.getApplicationContext());
        repositories=new Repositories(event.getApplicationContext());
    }

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody List<QuestionnaireInfo> list() {
        List<QuestionnaireInfo> infoList = listRepositories();
        int sum = 0;
        for(QuestionnaireInfo i : infoList) sum += i.getSize();
        exportLogRepository.save(new ExportLogDAO(sum));
        return (infoList);
    }

    @RequestMapping(value="{name}", method= RequestMethod.GET)
    public @ResponseBody List<Object> listData(@PathVariable String name) {
        JpaRepository rep = getRepositoryForName(name);
        if (rep != null) {
            LOG.info("Found " + rep.count() + " items to return .");
            return rep.findAll();
        }
        else return new ArrayList<>();
    }

    @RequestMapping(value="{name}/{id}", method=RequestMethod.DELETE)
    public @ResponseBody void delete(@PathVariable String name, @PathVariable long id) {
        Class<?> domainType = getDomainType(name);
        if (domainType != null) {
            if (domainType.isAnnotationPresent(DoNotDelete.class))
                throw new NotDeleteableException();
            JpaRepository rep = getRepositoryForName(name);
            rep.delete(id);
            rep.flush();
        } else {
            throw new NoSuchQuestionnaireException();
        }
    }

    /**
     * Returns a Class for a given name.  Makes some
     * assumptions about the class.  This method is pretty dreadful, returns null
     * if it can't find a repository by name.
     */
    private Class<?> getDomainType(String name) {
        if(repositories == null) return null;
        for (  Class<?> domainType : repositories) {
            if (domainType.getSimpleName().equals(name)) {
                return domainType;
            }
        }
        return null;
    }

    /**
     * Returns a repository for a given name.  Makes some
     * assumptions about the class.  This class is pretty dreadful, returns null
     * if it can't find a repository by name.
     */
    private JpaRepository getRepositoryForName(String name) {
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
            if(QuestionnaireData.class.isAssignableFrom(domainType)) {
                deleteableFlag        = !domainType.isAnnotationPresent(DoNotDelete.class);
                JpaRepository rep = (JpaRepository)repository;
                QuestionnaireInfo info = new QuestionnaireInfo(domainType.getSimpleName(), rep.count(), deleteableFlag);
                names.add(info);
            }
        }
        LOG.info("Returning: " + names);
        return names;
    }


}


