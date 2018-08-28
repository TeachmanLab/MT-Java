package org.mindtrails.service;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mindtrails.domain.*;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.importData.ImportError;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.domain.tracking.MissingDataLog;
import org.mindtrails.persistence.MissingDataLogRepository;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.*;


/**
 * Imports data from another instance, removing data from that system and loading it here.
 */
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

    @Value("${import.delete}")
    private boolean deleteMode;

    @Value("${import.mode}")
    private String mode;


    @Autowired ExportService exportService;

    @Autowired StudyRepository studyRepository;

    @Autowired ParticipantRepository participantRepository;

    @Autowired MissingDataLogRepository missingDataLogRepository;



    /** Just to help with testing, should be set with a config file in general */
    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isExporting() {
        LOGGER.info("The current mode is: " + this.mode);
        LOGGER.info("Is this exporting? " +this.mode.trim().toLowerCase().equals("export"));
        return this.mode.trim().toLowerCase().equals("export");
    }

    public boolean isImporting() {
        LOGGER.info("The current mode is: " + this.mode);
        LOGGER.info("Is this importing? " +this.mode.trim().toLowerCase().equals("import"));
        return this.mode.trim().toLowerCase().equals("import");
    }


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
        try {
            ResponseEntity<List<Scale>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, new ParameterizedTypeReference<List<Scale>>() {
            });
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

    public Boolean deleteOnline(String scale, long id) {
        LOGGER.info("Get into the deleteOnline function");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(path + "/api/export/" + scale + '/' + Long.toString(id));
        LOGGER.info("calling url:" + uri.toString());
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, request, new ParameterizedTypeReference<String>() {
        });
        LOGGER.info("Delete?");
        try {
            String response = responseEntity.getBody();
            if (response.isEmpty()) return true;
        } catch (HttpClientErrorException e) {
            throw new ImportError(e);
        }
        return false;
    }


    /**
     * This is the function that we used to log the null data in columns.
     *
     */

    public Boolean saveMissingLog(JsonNode jsonNode, String scale) {

        ObjectNode jMissingNode = new ObjectMapper().createObjectNode();
        Iterator<String> it = jsonNode.fieldNames();
        String fields = "";
        String participant="";
        String id = "";
        String date = "";
        Boolean found = false;
        while (it.hasNext())
        {
            String key = it.next();
            if(!Objects.equals(key, "tag")&&!Objects.equals(key, "button_pressed")){

            if(jsonNode.get(key).asText().equals("null")){
                //System.out.println("found :"+ key);
                fields = ";"+key;
                found = true;
            }
            }
        }
        if (found){
            if(jsonNode.has("date")){date=jsonNode.get("date").asText();}
            if(jsonNode.has("id")){id=jsonNode.get("id").asText();}
            if(jsonNode.has("participant")){participant=jsonNode.get("participant").asText();}
            Participant p = participantRepository.findOne(Long.parseLong(participant));
            MissingDataLog mlog = new MissingDataLog(p,scale,fields.substring(1),Long.parseLong(id),date);
            missingDataLogRepository.save(mlog);
            return true;
        }
        System.out.println(jMissingNode.get("columns"));
        return false;
    }






    /**
     * This is the function that used to delete information from the client site.
     */

    @ImportMode
    public Boolean safeDelete(String scale, long id) {
        LOGGER.info("Successfully launch the delete function");
        boolean deleteable = !exportService.getDomainType(scale).isAnnotationPresent(DoNotDelete.class);
        if (deleteMode && deleteable && validated(scale,id)) {
            return deleteOnline(scale,id);
        } else {return false;}
    }


    /**
     * This is the function that will validate the save result.
     */

    public Boolean validated(String scale, long id) {return true;}

    /**
     *
 *
 * Backup from local.
 *
 * */


    @ImportMode
    public int localBackup(String scale, File[] list) {
        LOGGER.info("Successfully launch local backup");
        int error = 0;
        if (list != null) {
            for (File is:list) {
                if (!parseDatabase(scale,readJSON(is))) error += 1;
            }
            LOGGER.info("Error: " + Integer.toString(error) + "/" + list.length);
        }
        return error;
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
        File[] files = folder.listFiles((dir,name) -> name.toLowerCase().startsWith(pattern));
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


    @ImportMode
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
                //safeDelete(clz.getName(),elm.path("id").asLong());
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
                //safeDelete(clz.getName(),elm.path("id").asLong());
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

    @ImportMode
    public boolean parseDatabase(String scale, String is){
        LOGGER.info("Get into the parseDatabase function");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JpaRepository rep = exportService.getRepositoryForName(scale);
        if (rep != null) {
            LOGGER.info("Found " + scale + " repository.");
            Class<?> clz = exportService.getDomainType(scale);
            if (clz != null) {
                LOGGER.info("Found " + clz.getName() + " class.");
                try {
                    JsonNode obj = mapper.readTree(is);
                    saveMissingLog(obj,scale);
                    if (hasStudy.class.isInstance(clz.newInstance())) {
                        return linkStudy(obj,clz,rep);
                    } else if (hasParticipant.class.isInstance(clz.newInstance())){
                        return linkParticipant(obj, clz, rep);
                    } else {
                        Iterator itr = obj.elements();
                        while (itr.hasNext()) {
                            JsonNode elm = (JsonNode) itr.next();
                            ObjectNode p = elm.deepCopy();
                            saveMissingLog(elm,clz.getName());
                            Object object = mapper.readValue(p.toString(),clz);
                            rep.save(object);
                     //       safeDelete(scale,elm.path("id").asLong());
                        };
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                };
                return true;
            };
            return false;
        };
        return false;
    }


    @ImportMode
    public int updateParticipantLocal() {
        LOGGER.info("Get into the updateParticipant function.");
        File[] files = getFileList("Participant");
        int error = 0;
        for (File file:files) {
            if(!saveParticipant(readJSON(file))) {
                error += 1;
            };
        }
        return error;
    }


    @ImportMode
    public int updateStudyLocal() {
        LOGGER.info("Get into the updateStudy function.");
        File[] files = getFileList("Study");
        int error = 0;
        for (File file:files) {
            if(!parseDatabase("study",readJSON(file))) {
                error += 1;
            }
        }
        return error;
    }

    @ImportMode
    public boolean updateParticipantOnline() {
        LOGGER.info("Get into the updateParticipant function");
        String newParticipant = getOnline(url,"ParticipantExportDAO");
        if (newParticipant != null) {
            LOGGER.info("Successfully read participant table");
            return saveParticipant(newParticipant);
        }
        return false;
    }

    @ImportMode
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

    @ImportMode
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
    @ImportMode
    //@Scheduled(cron = "0 * * * * *")
    public void backUpData() {
        LOGGER.info("Try to backup data from local.");
        int errorFile = 0;
        List<String> good = new ArrayList<String>();
        List<String> bad = new ArrayList<String>();
        List<Scale> list = importList(url);
        list.removeIf(s -> s.toString().startsWith("Participant"));
        list.removeIf(s -> s.toString().startsWith("Study"));
        list.removeIf(s -> s.toString().startsWith("Trial"));
        errorFile = errorFile + updateStudyLocal();
        errorFile = errorFile + updateParticipantLocal();
        for (Scale scale:list) {
            File[] is = getFileList(scale.getName());
            int outCome = localBackup(scale.getName(),is);
            if (outCome>0) {
                bad.add(scale.getName());
                errorFile = errorFile + outCome;
            } else {
                good.add(scale.getName());
            }
        }
        LOGGER.info("Here is the good list:");
        for (String flag:good) LOGGER.info(flag);
        LOGGER.info("Here is the bad list:");
        for (String flag:bad) LOGGER.info(flag);

    }


//    /**
//     * This is just an in-app testing. Need to be deleted before launch.
//     */
//    @ImportMode
//    //@Scheduled(cron = "0 * * * * *")
//    public void testingBackUp() {
//        LOGGER.info("Try to backup data from local.");
//        int errorFile = 0;
//        List<String> good = new ArrayList<String>();
//        List<String> bad = new ArrayList<String>();
//        List<String> list = Arrays.asList("ReasonsForEnding");
//        for (String scale:list) {
//            File[] is = getFileList(scale);
//            int outCome = localBackup(scale,is);
//            if (outCome>0) {
//                bad.add(scale);
//                errorFile = errorFile + outCome;
//            } else {
//                good.add(scale);
//            }
//        }
//        LOGGER.info("Here is the good list:");
//        for (String flag:good) LOGGER.info(flag);
//        LOGGER.info("Here is the bad list:");
//        for (String flag:bad) LOGGER.info(flag);
//
//    }
}

