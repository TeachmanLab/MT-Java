package org.mindtrails.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindtrails.domain.piPlayer.SequenceJson;
import org.mindtrails.domain.piPlayer.Trial;
import org.mindtrails.domain.piPlayer.TrialJson;
import org.mindtrails.persistence.TrialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * This provides the web interface for accepting data from PI Player.
 */
@Controller
@RequestMapping("/data")
public class TrialController {

    private final TrialRepository trialRepository;

    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public TrialController(TrialRepository repository) {
        this.trialRepository   = repository;
    }

    /**
     * This is the proper REST endpoint, but we can't use it because the PIPlayer doesn't presently
     * send data back in the standard way.
     * @param trialJson
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String createData(@RequestBody TrialJson trialJson) {
        System.out.println("Recieved Data:" + trialJson);
        return "Thank you.";
    }

    /**
     * This is the endpoint currently in use, it accepts the post from the PI Player,
     * converts the json data to a TrialDAO object and saves that TrialDAO to the database.
     * @param json
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,
            headers = "content-type=application/x-www-form-urlencoded")
    public@ResponseBody String createDataFromForm(@RequestParam("json") String json) {
        SequenceJson sequenceJson = toSequence(json);
        for(TrialJson trialJson: sequenceJson) {
            System.out.println("Recieved Data:" + trialJson);
            this.trialRepository.save(new Trial(trialJson));
        }
        return "Thank you.";
    }

    /**
     * Converts the json to a sequence (a list of TrialDAO objects)
     * @param text
     * @return
     * @throws IllegalArgumentException
     */
    public SequenceJson toSequence(String text) throws IllegalArgumentException {
        ObjectMapper mapper   = new ObjectMapper();
        SequenceJson sequenceJson = new SequenceJson();
        try {
            sequenceJson = mapper.readValue(text, SequenceJson.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JsonParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return sequenceJson;
    }

}

