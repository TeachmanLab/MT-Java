package org.mindtrails.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.jni.Error;
import org.mindtrails.domain.importData.ImportError;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mindtrails.domain.importData.Scale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.net.URI;


/**
 * Used to award a gift certificates to participants.
 *
 * Documentation is available here: https://github.com/tangocarddev/RaaS
 * There is great tool for working directly with the API here: https://integration-www.tangocard.com/raas_api_console/
 * Settings are read in from the file here: resources/application.properties, just look for the section on Tango.
 *
 * If you need to add money to the account for testing, the fake credit card we have setup has a cid of 32733202
 *
 *
 */

//@Data
@Service
public class ImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportService.class);

    @Value("${import.username}")
    private String username;

    @Value("${import.password}")
    private String password;

    @Value("${import.url}")
    private String url;

    @Value("${import.path}")
    private String path;

    @Autowired ExportService exportService;


/**
 *  Class finder.
 * */



    /**
     * Setup the headers for authorization.
     *
     * */

    private HttpHeaders headers() {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

/**
 * Here is the function to get a complete list of scale from api/export.
 *
 * */
    public List<Scale> importList() {
        LOGGER.info("Get into the original methods");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(url + "api/export/");
        LOGGER.info("calling url:" + uri.toString());
        ResponseEntity<List<Scale>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<List<Scale>>() {
        });
        LOGGER.info("Get something?");
        try {
            List<Scale> response = responseEntity.getBody();
            return response;
        } catch (HttpClientErrorException e) { throw new ImportError(e);}
    }



    /**
     *  The function that can get data from the online api, according to the name you fill
     *  in.
     *
     * */
    public String getOnline(String scale) {
        LOGGER.info("Get into the getOnline function");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(url + "api/export/" + scale);
        LOGGER.info("calling url:" + uri.toString());
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<String>() {
        });
        LOGGER.info("Get some items?");
        try {
            String response = responseEntity.getBody();
            return response;
        } catch (HttpClientErrorException e) {
            throw new ImportError(e);
        }
    }


/**
 *
 *
 * Backup from local.
 *
 * */


    public boolean localBackup(String scale, List<String> list) {
        LOGGER.info("Successfully launch local backup");
        int error = 0;
        if (list != null) {
            for (String is:list) {
                if (!parseDatabase(scale,is)) error += 1;
            }
            LOGGER.info("Error: " + Integer.toString(error) + "/" + Integer.toString(list.size()));
            if (list.size()>error) return true;
        }
        return false;
    }

    /**
     *
     * Get data from local folder, according to the name you fill in.
     * */

    public String readJSON(File file){
        LOGGER.info("Try to read a JSON file");
        try {
            String contents = new String(Files.readAllBytes(file.toPath()));
            return contents;
        } catch (IOException e) {
            LOGGER.error(e.toString());
            return null;
        }
    }


    public List<String> getLocal(String scale) {
        LOGGER.info("Get into the getLocal function");
        File folder = new File(path);
        String pattern = scale.toLowerCase();
        File[] files = folder.listFiles((dir,name) -> name.toLowerCase().contains(pattern));
        LOGGER.info("Here are the files that I found:" + files.toString());
        List<String> list = new ArrayList<String>();
        for (File file:files) {
            list.add(readJSON(file));
        }
        return list;
    }

    /**
     * Get the type/class/object for a name. This is much more difficult that I thought.
     *
     * */

    public Class<?> getClass(String scale) {
        LOGGER.info("What happens here?");
        Class<?> clz = exportService.getDomainType(scale);
        if (clz != null) {
            LOGGER.info(clz.getName());
            return clz;
        }
        LOGGER.info("Did not find it.");
        return null;
    }

/**
 * parse the data you get into the database.
 *
 * */

    public boolean parseDatabase(String scale, String is){
        LOGGER.info("Get into the parseDatabase function");
        ObjectMapper mapper = new ObjectMapper();
        JpaRepository rep = exportService.getRepositoryForName(scale);
        if (rep != null) {
            LOGGER.info("Found " + scale + " repository.");
            Class<?> clz = exportService.getDomainType(scale);
            if (clz != null) {
                LOGGER.info("Found " + clz.getName() + " class.");
                JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clz);
                LOGGER.info("Successfully created mapper.");
                try {
                    List<?> list = new ArrayList<>();
                    list = mapper.readValue(is, type);
                    LOGGER.info("Successfully created list for data.");
                    rep.save(list);
                    LOGGER.info("List saved successfully");
                    return true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            };
            return false;
        };
        return false;
    }

/**
 *  Every five minutes the program will try to download all the data.
 * */

    @Scheduled(cron = "0 * * * * *")
    public void importData() {
        LOGGER.info("Trying to download data from api/export.");
        int i = 0;
        List<String> good = new ArrayList<String>();
        List<String> bad = new ArrayList<String>();
        List<Scale> list = importList();
        for (Scale scale:list) {
            String is = getOnline(scale.getName());
            if (parseDatabase(scale.getName(),is)) {
                i += 1;
                good.add(scale.getName());
            } else {
                bad.add(scale.getName());
            }
        }
        LOGGER.info("Here is the good list:");
        for (String flag:good) LOGGER.info(flag);
        LOGGER.info("Here is the bad list:");
        for (String flag:bad) LOGGER.info(flag);
        LOGGER.info("Let's review all the error messages:");
        for (String flag:bad) {
            String is = getOnline(flag);
            parseDatabase(flag,is);
        };
    }

    /**
     *
     *
     * The backup routine.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void backUpData() {
        LOGGER.info("Try to backup data from local.");
        int i = 0;
        List<String> good = new ArrayList<String>();
        List<String> bad = new ArrayList<String>();
        List<Scale> list = importList();
        for (Scale scale:list) {
            List<String> is = getLocal(scale.getName());
            if (localBackup(scale.getName(),is)) {
                i += 1;
                good.add(scale.getName());
            } else {
                bad.add(scale.getName());
            }
        }
        LOGGER.info("Here is the good list:");
        for (String flag:good) LOGGER.info(flag);
        LOGGER.info("Here is the bad list:");
        for (String flag:bad) LOGGER.info(flag);

    }
}
