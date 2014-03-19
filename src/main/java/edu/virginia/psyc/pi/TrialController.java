package edu.virginia.psyc.pi;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.pi.DAO.TrialDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/27/14
 * Time: 11:21 AM
 * This provides the web interface for accepting data from PI Player and returning the results
 * as JSON from the database.
 */
@Controller
@RequestMapping("/data")
public class TrialController {

    private TrialDAO trialDAO;

    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public TrialController(TrialDAO dao) {
        this.trialDAO     = dao;
    }

    /**
     * This is the proper REST endpoint, but we can't use it because the PIPlayer doesn't presently
     * send data back in the standard way.
     * @param trial
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String createData(@RequestBody Trial trial) {
        System.out.println("Recieved Data:" + trial);
        return "Thank you.";
    }

    /**
     * This is the endpoint currently in use, it accepts the post from the PI Player,
     * converts the json data to a Trial object and saves that Trial to the database.
     * @param json
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,
            headers = "content-type=application/x-www-form-urlencoded")
    public@ResponseBody String createDataFromForm(@RequestParam("json") String json) {
        Sequence sequence = toSequence(json);
        System.out.println("Recieved sequence:" + sequence);
        for(Trial trial : sequence) {
            System.out.println("Recieved Data:" + trial);
            this.trialDAO.insert(trial);
        }
        return "Thank you.";
    }

    /**
     * Converts the json to a sequence (a list of Trial objects)
     * @param text
     * @return
     * @throws IllegalArgumentException
     */
    public Sequence toSequence(String text) throws IllegalArgumentException {
        ObjectMapper mapper   = new ObjectMapper();
        Sequence     sequence = new Sequence();
        try {
            sequence = mapper.readValue(text, Sequence.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JsonParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return sequence;
    }


    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Trial> getData () {
        return trialDAO.getTrials();
    }

}

