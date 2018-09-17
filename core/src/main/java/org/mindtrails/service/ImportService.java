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
import org.mindtrails.persistence.StudyExportRepository;
import org.mindtrails.persistence.StudyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
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
import java.io.InputStream;
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


    @Autowired
    ExportService exportService;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    MissingDataLogRepository missingDataLogRepository;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * Just to help with testing, should be set with a config file in general
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isExporting() {
        return this.mode.trim().toLowerCase().equals("export");
    }

    public boolean isImporting() {
        return this.mode.trim().toLowerCase().equals("import");
    }


    /**
     * Setup the headers for authorization.
     */

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
     * Calls the API to get a list of all scales to import.
     */
    public List<Scale> fetchListOfScales() throws HttpClientErrorException {
        HttpEntity<String> request = new HttpEntity<String>(headers());
        URI uri = URI.create(this.url + "/api/export");
        try {
            ResponseEntity<List<Scale>> responseEntity =
                    restTemplate.exchange(uri, HttpMethod.GET, request,
                            new ParameterizedTypeReference<List<Scale>>() {
                            });
;            List<Scale> scales = responseEntity.getBody();
            // do a bit of reordering, as we have to import study first, then participant, then
            // everything else.
            moveScaleToTopOfList(scales, "ParticipantExport");
            moveScaleToTopOfList(scales, "StudyImportExport");
            return scales;
        } catch(RestClientException rce) {
            throw new ImportError("Failed to get the proper response back.  " +
                    "Do you have the correct credentials to connect to the server?");
        }

    }

    public void moveScaleToTopOfList(List<Scale> list, String name) {
        Scale s = list.stream().filter(scale -> scale.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if(null != s) {
            list.remove(s);
            list.add(0, s);
        }
    }

    /**
     * Call the API and get all data on a given scale.
     */
    public InputStream fetchScale(String scale) {
        HttpEntity<String> request = new HttpEntity<>(headers());
        URI uri = URI.create(this.url + "/api/export/" + scale);
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request,
                Resource.class);
        try {
            return responseEntity.getBody().getInputStream();
        } catch (HttpClientErrorException | IOException e) {
            throw new ImportError(e);
        }
    }

    public void deleteScaleItem(String scale, long id) {
        try {
            boolean deleteable = !exportService.getDomainType(scale, false).isAnnotationPresent(DoNotDelete.class);
            if(!deleteable) return;
            HttpEntity<String> request = new HttpEntity<String>(headers());
            URI uri = URI.create(url + "/api/export/" + scale + '/' + Long.toString(id));
            restTemplate.exchange(uri, HttpMethod.DELETE, request, new ParameterizedTypeReference<String>() {
            });
        } catch (HttpClientErrorException | NullPointerException e) {
            throw new ImportError(e);
        }
    }

    /**
     * This is the function that will validate the save result.
     */

    public Boolean validated(String scale, long id) {
        return true;
    }


    /**
     * Given all data on a scale as an input stream returned from the API, parse this string and
     * feed it into the database.
     */
    @ImportMode
    public void importScale(String scale, InputStream is) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JpaRepository rep = exportService.getRepositoryForName(scale, false);
        if (rep == null) {
            throw new ImportError("No Repository exists for scale:" + scale);
        }
        Class<?> clz = exportService.getDomainType(scale, false);
        if (clz == null) {
            throw new ImportError("No class could be found for scale:" + scale);
        }
        try {
            JsonNode obj = mapper.readTree(is);
            saveMissingLog(obj, scale);  // TODO: Strip this out.  It doesn't make sense.
            if (obj.isArray()) {
                Iterator itr = obj.elements();
                while (itr.hasNext()) {
                    JsonNode elm = (JsonNode) itr.next();
                    this.importNode(elm, clz, mapper, rep, scale);
                }
            } else {
                this.importNode(obj, clz, mapper, rep, scale);
            }
        } catch (IOException e) {
            throw new ImportError(e);
        } finally {
            rep.flush();
        }
    }

    /**
     * Import a single node in a json array into the database
     * @param elm
     * @param clz
     * @param mapper
     * @param rep
     * @param scale
     * @throws IOException
     */
    private void importNode(JsonNode elm, Class clz, ObjectMapper mapper, JpaRepository rep, String scale) throws IOException {
        ObjectNode node = elm.deepCopy();
        saveMissingLog(elm, clz.getName()); // TODO: Strip this out.  Do something sensible.
        long studyId = -1;
        long participantId = -1;

        if (elm.has("study")) {
            studyId = elm.path("study").asLong();
            node.remove("study");
        }
        if (elm.has("participant")) {
            participantId = elm.path("participant").asLong();
            node.remove("participant");
        }
        Object object = mapper.readValue(node.toString(), clz);
        if (object instanceof HasStudy) {
            ((HasStudy) object).setStudy(studyRepository.findById(studyId));
        }
        if (object instanceof hasParticipant) {
            ((hasParticipant) object).setParticipant(participantRepository.findOne(participantId));
        }
        rep.save(object);
        //safeDelete(scale,elm.path("id").asLong());
    }


    /**
     * Every five minutes the program will try to download all the data.
     */
    @Scheduled(fixedRateString = "${import.rate.in.milliseconds}")
    public void importData() {
        if(!this.isImporting()) { return; }
        LOGGER.info("Importing data from " + this.url);
        List<Scale> list = fetchListOfScales();
        for (Scale scale : list) {
            importScale(scale.getName(), fetchScale(scale.getName()));
        }
    }

    /**
     * File System Import
     * ================================================================
     */
    @ImportMode
    //@Scheduled(cron = "0 * * * * *")
    public void backUpData() {
        int errorFile = 0;
        List<Scale> list = fetchListOfScales();
        for (Scale scale : list) {
            File[] is = getFileList(scale.getName());
            localBackup(scale.getName(), is);
        }
    }

    /** Imports a scale from a list of local files
     *
     * @param scale
     * @param list
     * @return
     */
    @ImportMode
    public void localBackup(String scale, File[] list) {
        if (list != null) {
            for (File file : list) {
                try {
                    importScale(scale, Files.newInputStream(file.toPath()));
                } catch (ImportError | IOException e) {
                    throw new ImportError(e);
                }
            }
        }
    }

    public File[] getFileList(String scale) {
        File folder = new File(path);
        String pattern = scale.toLowerCase();
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().startsWith(pattern));
        return files;
    }



    /**
     * Missing Data Tracking
     * ================================================================
     */

    /**
     * This is the function that we used to log the null data in columns.
     */
    public Boolean saveMissingLog(JsonNode jsonNode, String scale) {
        return(false);
        // This created over 1/2 million records in the database during
        // a few hours of rapidly running the importer rapidly.
        /*
        ObjectNode jMissingNode = new ObjectMapper().createObjectNode();
        Iterator<String> it = jsonNode.fieldNames();
        String fields = "";
        long participantId;
        String id = "";
        String date = "";
        Boolean found = false;

        while (it.hasNext()) {
            String key = it.next();
            if (!Objects.equals(key, "tag") && !Objects.equals(key, "button_pressed")) {

                if (jsonNode.get(key).asText().equals("null")) {
                    //System.out.println("found :"+ key);
                    fields = ";" + key;
                    found = true;
                }
            }
        }

        if (found) {
            if (jsonNode.has("date")) {
                date = jsonNode.get("date").asText();
            }
            if (jsonNode.has("id")) {
                id = jsonNode.get("id").asText();
            }
            if (jsonNode.has("participant") && jsonNode.get("participant").asLong() > 0) {
                participantId = jsonNode.get("participant").asLong();
                Participant p = participantRepository.findOne(participantId);
                MissingDataLog mlog = new MissingDataLog(p, scale, fields.substring(1), Long.parseLong(id), date);
                missingDataLogRepository.save(mlog);
            }
            return true;
        }
        System.out.println(jMissingNode.get("columns"));
        return false;
        */
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

