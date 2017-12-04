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
import org.mindtrails.service.ExportService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

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
public class ImportService {

    private static final Logger LOG = LoggerFactory.getLogger(ImportService.class);

    @Autowired ExportService exportService;

    /**
     * Returns a list for a given name.  Makes some
     * assumptions about the class.  This class is pretty dreadful, returns null
     * if it can't find a list by name.
     */

    private static <T> List<T> createListOfType(T element){
        return new ArrayList<T>();
    }

    /**
     *
     *
     * Need to finish this.
     */

    public List getListForName(String name) {
        Class<?> domainType = exportService.getDomainType(name);
        if (domainType != null)
            return (List) repositories.getRepositoryFor(domainType);
        LOG.info("failed to create a list for " + name);
        return null;
    }

    /**
     *
     * Need to finish this.
     */

    public void localImport(String name) {
        ObjectMapper mapper = new ObjectMapper();
        JpaRepository rep = exportService.getRepositoryForName(name);
        if (rep != null) {
            LOG.info("Found " + name + " repository.");
            TypeReference<List<State>> mapType = new TypeReference<List<State>>() {
            };
            InputStream is = TypeReference.class.getResourceAsStream("/json/state-city.json");
            try {
                List<State> stateList = mapper.readValue(is, mapType);
                stateRepository.save(stateList);
                System.out.println("list saved successfully");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }



}