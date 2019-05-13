package org.mindtrails.service;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.utils.URIBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.mindtrails.domain.*;
import org.mindtrails.domain.Conditions.ConditionAssignment;
import org.mindtrails.domain.Conditions.NoNewConditionException;
import org.mindtrails.domain.Conditions.RandomCondition;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.importData.ImportError;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.domain.questionnaire.QuestionnaireData;
import org.mindtrails.domain.tracking.ImportLog;
import org.mindtrails.persistence.ImportLogRepository;
import org.mindtrails.persistence.MissingDataLogRepository;
import org.mindtrails.persistence.ParticipantRepository;
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

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    ParticipantService participantService;

    @Autowired
    MissingDataLogRepository missingDataLogRepository;

    @Autowired
    ImportLogRepository importLogRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EntityManager entityManager;

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
    public InputStream fetchScale(String scale)  {
        HttpEntity<String> request = new HttpEntity<>(headers());

        // Find the last time data was successfully exported for this scale.
        ImportLog log = importLogRepository.findFirstByScaleAndSuccessfulOrderByDateStartedDesc(scale, true);

        // Create url with a data parmeter
        try {
            URIBuilder b = new URIBuilder(this.url + "/api/export/" + scale);
            // add a date parameter if we have successuflly pulled from this scale in the past.
            if(null != log) {
                DateTime sinceDate = new DateTime(log.getDateStarted());
                // Step it back five minutes, as extra safely to assure we don't miss anything.
                sinceDate = sinceDate.minus(Minutes.minutes(5));
                DateFormat formatter = new SimpleDateFormat(ExportService.DATE_FORMAT);
                b.addParameter("after", formatter.format(sinceDate.toDate()));
            }
            URI uri = b.build();
            ResponseEntity<Resource> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request,
                Resource.class);
            return responseEntity.getBody().getInputStream();
        } catch (HttpClientErrorException | IOException | URISyntaxException e) {
            throw new ImportError(e);
        }
    }

    public boolean deleteable(String scale) {
        return !exportService.getDomainType(scale, false).isAnnotationPresent(DoNotDelete.class);
    }

    public void deleteScaleItem(String scale, long id) {
        try {
            if(!this.deleteable(scale) || !deleteMode) return;

            HttpEntity<String> request = new HttpEntity<String>(headers());
            URI uri = URI.create(url + "/api/export/" + scale + '/' + Long.toString(id));
            restTemplate.exchange(uri, HttpMethod.DELETE, request, new ParameterizedTypeReference<String>() {
            });
        } catch (HttpClientErrorException | NullPointerException e) {
            LOGGER.info("Failed to delete " + scale + " item #" + id);
            //throw new ImportError(e);
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
        ImportLog log = new ImportLog(scale);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JpaRepository rep = exportService.getRepositoryForName(scale, false);
        if (rep == null) {
            String message = "No Repository exists for scale:" + scale;
            throw new ImportError(message);
        }
        try {
            Class<?> clz = exportService.getDomainType(scale, false);
            if (clz == null) {
                String message = "No class could be found for scale:" + scale;
                throw new ImportError(message);
            }
            JsonNode obj = mapper.readTree(is);
            if (obj.isArray()) {
                Iterator itr = obj.elements();
                while (itr.hasNext()) {
                    JsonNode elm = (JsonNode) itr.next();
                    try {
                        this.importNode(elm, clz, mapper, rep, scale);
                        log.incrementSuccess();
                    } catch (IOException ioe) {
                        log.setError("Node Import Failed:" + ioe.getMessage());
                        log.incrementError();
                    }
                }
            } else {
                try {
                    this.importNode(obj, clz, mapper, rep, scale);
                    log.incrementSuccess();
                } catch (IOException ioe) {
                    LOGGER.error("Node Import Failed:" + ioe.getMessage());
                    log.setError("Node Import Failed:" + ioe.getMessage());
                    log.incrementError();
                }
            }
        } catch (Exception e) {
            String message = "Exception encountered loading  scale " + scale + ". " + e.getMessage();
            LOGGER.error(message);
            log.setError(message);
        } finally {
            if(log.worthSaving()) {
                importLogRepository.save(log);
                rep.flush();
            }
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
        long studyId = -1;
        long participantId = -1;
        Participant participant = null;

        if (elm.has("study")) {
            studyId = elm.path("study").asLong();
            node.remove("study");
        }
        if (elm.has("participant") && !elm.path("participant").asText().equals("null")) {
            participantId = elm.path("participant").asLong();
            // Request a proxy for the particpant, much much faster than loading the thing up with .find(id)
            participant = entityManager.getReference(Participant.class, participantId);
            node.remove("participant");
        }

        Object object = mapper.readValue(node.toString(), clz);
        if (object instanceof HasStudy) {
            ((HasStudy) object).setStudy(studyRepository.findById(studyId));
        }
        if (object instanceof hasParticipant) {
            ((hasParticipant) object).setParticipant(participant);
        }
        rep.save(object);
        long id = elm.path("id").asLong();

        LOGGER.info("Adding " + scale + " id " + id);
        if(this.deleteable(scale)) {
            LOGGER.info("Deleteing remote  " + scale + " id " + id);
            if(object instanceof hasParticipant && participant != null) {
                deleteScaleItem(scale, id);
            } else if (participantId > 0){
                LOGGER.error("Scale id# " + id + " in scale " + scale + " references a participant #" + participantId +
                "that does not exist in the database.");
            }
        }
    }

    private void clearDataForScale(Scale scale) {
        if(!scale.isDeleteable()) return;
        JpaRepository rep = exportService.getRepositoryForName(scale.getName(), false);
        List items = rep.findAll();
        for(Object i : items) {
            QuestionnaireData qd  = (QuestionnaireData)i;
            this.deleteScaleItem(scale.getName(), qd.getId());
        }

    }


    /**
     * DANGER METHOD.  This just clears the data, and doesn't check to see if it exists.
     */
    public void clearOldData() {
        if (!this.isImporting()) {
            return;
        }
        LOGGER.info("CLEARING ALL data from " + this.url);
        List<Scale> list = fetchListOfScales();
        for (Scale scale : list) {
            clearDataForScale(scale);
        }
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
        // after importing data, update condition assignments for any participants that have updates.
        updateConditionAssignments();
    }


    /**
     * Using select information only available on the import/secure side,
     * determine the condition to assign a participant, and post this
     * information back to the export server.
     */
    public void updateConditionAssignments() {

        List<Participant> participants = participantRepository.findByActive(true);
        RandomCondition assignment;

        // loop through the participants to see if they should be assigned a condition.
        for (Participant p : participants) {
            try {
                assignment = participantService.getCondition(p);
                updateConditionOnMainServer(p.getId(), assignment.getValue());
                // if successful, remove the assignment.
                participantService.markConditionAsUsed(assignment);
            } catch (NoNewConditionException e1) {
                // No reason to continue, no new condition is assignable to the participant at this time,
                // this should happen relatively rarely, but may occur repeatedly for participants that
                // signup and never complete their first session.
            } catch (IOException e) {
                e.printStackTrace();
                // We'll have to try again later, failed to communicate this update to front end server.
            }
        }
    }

    private void updateConditionOnMainServer(Long id, String condition) throws IOException {
        URI uri = URI.create(this.url + "/api/export/condition");
        ConditionAssignment ca = new ConditionAssignment(id, condition);
        HttpEntity<ConditionAssignment> entity = new HttpEntity<>(ca, headers());
        ResponseEntity<ConditionAssignment> response = restTemplate.postForEntity(uri, entity, ConditionAssignment.class);
        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new IOException("failed to update the main server.");
        }
    }


    /**
     * File System Import
     * ================================================================
     */
    @ImportMode
    //@Scheduled(cron = "0 * * * * *")
    public void backUpData() throws IOException {
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
    public void localBackup(String scale, File[] list) throws IOException {
        if (list != null) {
            for (File file : list) {
                importScale(scale, Files.newInputStream(file.toPath()));
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

