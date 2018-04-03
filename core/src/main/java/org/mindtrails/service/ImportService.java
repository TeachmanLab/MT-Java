package org.mindtrails.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mindtrails.domain.*;
import org.mindtrails.domain.importData.ImportError;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.persistence.StudyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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

    @Autowired StudyRepository studyRepository;

    @Autowired ParticipantRepository participantRepository;



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
    public List<Scale> importList(String path) {
        LOGGER.info("Get into the original methods");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(path + "/api/export/");
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
    public String getOnline(String path, String scale) {
        LOGGER.info("Get into the getOnline function");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(path + "/api/export/" + scale);
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
     * This is the function that used to delete information from the client site.
     * @param path
     * @param scale
     * @return
     */

    @DataOnly
    public Boolean deleteOnline(String path, String scale) {
        return false;
    }

/**
 *
 *
 * Backup from local.
 *
 * */


    @DataOnly
    public boolean localBackup(String scale, File[] list) {
        LOGGER.info("Successfully launch local backup");
        int error = 0;
        if (list != null) {
            for (File is:list) {
                if (!parseDatabase(scale,readJSON(is))) error += 1;
            }
            LOGGER.info("Error: " + Integer.toString(error) + "/" + list.length);
            if (list.length>error) return true;
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


    public File[] getFileList(String scale) {
        LOGGER.info("Get into the getLocal function");
        File folder = new File(path);
        String pattern = scale.toLowerCase();
        File[] files = folder.listFiles((dir,name) -> name.toLowerCase().contains(pattern));
        LOGGER.info("Here are the files that I found:" + files.toString());
        return files;
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


    /***
     * Saving the participant data
     */


    @DataOnly
    public boolean saveParticipant(String is){
        LOGGER.info("Try to save the participant table after saving the study table.");
        ObjectMapper mapper = new ObjectMapper();
        JpaRepository rep = exportService.getRepositoryForName("participant");
        if (rep != null) {
            Class<?> clz = exportService.getDomainType("participant");
            if (clz != null) {
                try {
                    JsonNode pObj = mapper.readTree(is);
                    Iterator itr = pObj.elements();
                    while (itr.hasNext()) {
                        JsonNode elm = (JsonNode) itr.next();
                        long index = elm.path("study").asLong();
                        try {
                            Study s = studyRepository.findById(index);
                            ObjectNode p = elm.deepCopy();
                            p.remove("study");
                            Participant participant = mapper.readValue(p.toString(), Participant.class);
                            participant.setStudy(s);
                            rep.save(participant);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }
        return false;
    }




    /**
     *
     * Save all the questionnaire and mindtrails logs
     */

    public boolean linkParticipant(JsonNode obj, Class clz, JpaRepository rep) {
        LOGGER.info("Try to link a questionnaire or log with the participant");
        Iterator itr = obj.elements();
        ObjectMapper mapper = new ObjectMapper();
        while (itr.hasNext()) {
            JsonNode elm = (JsonNode) itr.next();
            long index = elm.path("participant").asLong();
            try {
                Participant s = participantRepository.findOne(index);
                ObjectNode p = elm.deepCopy();
                p.remove("participant");
                Object object = mapper.readValue(p.toString(),clz);
                if (object instanceof hasParticipant) ((hasParticipant) object).setParticipant(s);
                rep.save(object);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;

    }


    /**
     *
     * Save all the questionnaire and mindtrails logs
     */


    public boolean linkStudy(JsonNode obj, Class clz, JpaRepository rep) {
        LOGGER.info("Try to link a questionnaire or log with the study");
        Iterator itr = obj.elements();
        ObjectMapper mapper = new ObjectMapper();
        while (itr.hasNext()) {
            JsonNode elm = (JsonNode) itr.next();
            long index = elm.path("study").asLong();
            try {
                Study s = studyRepository.findById(index);
                ObjectNode p = elm.deepCopy();
            //    p.set("study",null);
                Object object = mapper.readValue(p.toString(),clz);
                if (object instanceof hasStudy) ((hasStudy) object).setStudy(s);
                rep.save(object);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;

    }

    /**
     * parse the data you get into the database.
     *
     * */

    @DataOnly
    public boolean parseDatabase(String scale, String is){
        LOGGER.info("Get into the parseDatabase function");
        ObjectMapper mapper = new ObjectMapper();
        JpaRepository rep = exportService.getRepositoryForName(scale);
        if (rep != null) {
            LOGGER.info("Found " + scale + " repository.");
            Class<?> clz = exportService.getDomainType(scale);
            if (clz != null) {
                LOGGER.info("Found " + clz.getName() + " class.");
                try {
                    JsonNode obj = mapper.readTree(is);
                    if (hasStudy.class.isInstance(clz.newInstance())) {
                        return linkStudy(obj,clz,rep);
                    } else if (hasParticipant.class.isInstance(clz.newInstance())){
                        return linkParticipant(obj, clz, rep);
                    } else {
                        Iterator itr = obj.elements();
                        while (itr.hasNext()) {
                            JsonNode elm = (JsonNode) itr.next();
                            ObjectNode p = elm.deepCopy();
                            Object object = mapper.readValue(p.toString(),clz);
                            rep.save(object);
                        };
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                };
                return true;
            };
        };
        return false;
    }


    @DataOnly
    public boolean updateParticipantLocal() {
        LOGGER.info("Get into the updateParticipant function.");
        File[] files = getFileList("ParticipantExportDAO");
        for (File file:files) {
            return parseDatabase("ParticipantExportDAO",readJSON(file));
        }
        return false;
    }


    @DataOnly
    public boolean updateStudyLocal() {
        LOGGER.info("Get into the updateStudy function.");
        File[] files = getFileList("StudyExportDAO");
        for (File file:files) {
            return parseDatabase("StudyExportDAO",readJSON(file));
        }
        return false;
    }

    @DataOnly
    public boolean updateParticipantOnline() {
        LOGGER.info("Get into the updateParticipant function");
        String newParticipant = getOnline(url,"ParticipantExportDAO");
        if (newParticipant != null) {
            LOGGER.info("Successfully read participant table");
            return saveParticipant(newParticipant);
        }
        return false;
    }

    @DataOnly
    public boolean updateStudyOnline() {
        LOGGER.info("Get into the updatestudy function");
        String newStudy = getOnline(url,"study");
        if (newStudy != null) {
            LOGGER.info("Successfully read study table");
            return parseDatabase("study",newStudy);
        }
        return false;
    }



/**
 *  Every five minutes the program will try to download all the data.
 * */

    @DataOnly
    @Scheduled(cron = "0 5 * * * *")
    public void importData() {
        LOGGER.info("Trying to download data from api/export.");
        boolean newStudy = updateStudyOnline();
        if (newStudy) LOGGER.info("Successfully logged new studies");
        boolean newParticipant = updateParticipantOnline();
        if (newParticipant) LOGGER.info("Successfully logged new participants");
        int i = 0;
        List<String> good = new ArrayList<String>();
        List<String> bad = new ArrayList<String>();
        List<Scale> list = importList(url);
        for (Scale scale:list) {
            if (!scale.getName().toLowerCase().contains("exportdao")) {
                String is = getOnline(url, scale.getName());
                if (parseDatabase(scale.getName(), is)) {
                    i += 1;
                    good.add(scale.getName());
                } else {
                    bad.add(scale.getName());
                }
            }
        }
        LOGGER.info("Here is the good list:");
        for (String flag:good) LOGGER.info(flag);
        LOGGER.info("Here is the bad list:");
        for (String flag:bad) LOGGER.info(flag);
        LOGGER.info("Let's review all the error messages:");
    }

    /**
     *
     *
     * The backup routine.
     */
    @DataOnly
    @Scheduled(cron = "0 0 0 * * *")
    public void backUpData() {
        LOGGER.info("Try to backup data from local.");
        int i = 0;
        List<String> good = new ArrayList<String>();
        List<String> bad = new ArrayList<String>();
        List<Scale> list = importList(url);
        for (Scale scale:list) {
            File[] is = getFileList(scale.getName());
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

