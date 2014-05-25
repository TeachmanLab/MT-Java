package edu.virginia.psyc.pi.mvc;

import edu.virginia.psyc.pi.mvc.model.Participant;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/questions")
public class QuestionController {

    private ParticipantRepository participantRepository;
    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public QuestionController(ParticipantRepository repository) {
        this.participantRepository   = repository;
    }


    @RequestMapping(value="{name}", method=RequestMethod.GET)
    public String showQuestionnaire(ModelMap model,
                           @PathVariable("name") String name) {
        LOG.info("Showing Questionnaire " + name);
        return "/questions/" + name;
    }

}
