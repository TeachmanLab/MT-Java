package org.mindtrails.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JavaType;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io;



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
     *
     * Read json file locally and parse it into the database. Don't know how to write it. Damm!
     */

    public List<String> listJSON(String name, String path) {
        List<String> jsonFiles = new ArrayList<>();
        Collection files = FileUtils.listFiles(
                dir,
                new RegexFileFilter("^(.*?)"),
                DirectoryFileFilter.DIRECTORY
        );

    }

    /**
     *
     * @param name
     * @param path
     *
     * This is a function to recover all json files from local. Not yet finished.
     */

    public void localImport(String name, String path) {
        ObjectMapper mapper = new ObjectMapper();
        JpaRepository rep = exportService.getRepositoryForName(name);
        List<String> fileList = listJSON(name,path);
        if (rep != null) {
            LOG.info("Found " + name + " repository.");
          /**  TypeReference<List<State>> mapType = new TypeReference<List<State>>() {
            };
           */
            Class<?> clz = Class.forName(name);
            if (clz != null) {
                JavaType type = mapper.getTypeFactory().
                        constructCollectionType(List.class, clz);
                for (String fileName : fileList) {
                    InputStream is = TypeReference.class.getResourceAsStream(fileName);
                    try {
                        List<?> list = mapper.readValue(is, type);
                        rep.save(list);
                        System.out.println("list saved successfully");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     *
     * @param name
     * @param is
     *
     * This is the function to import data from the server. Not yet tested.
     */

    public void liveImport(String name, String is) {
        ObjectMapper mapper = new ObjectMapper();
        JpaRepository rep = exportService.getRepositoryForName(name);
        if (rep != null) {
            LOG.info("Found " + name + " repository.");
            Class<?> clz = Class.forName(name);
            if (clz != null) {
                JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clz);
                try {
                    List<?> list = mapper.readValue(is, type);
                    rep.save(list);
                    System.out.println("List saved successfully");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }


    public String jsonGetter()

}

