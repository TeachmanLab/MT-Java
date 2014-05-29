package edu.virginia.psyc.pi.mvc;

import edu.virginia.psyc.pi.mvc.model.Participant;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.Questionnaire.*;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

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

    private DASS21_ASRepository   dass21_asRepository;
    private CredibilityRepository credibilityRepository;

    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public QuestionController(DASS21_ASRepository dass21_asRepository,
                              CredibilityRepository credibilityRepository) {
        this.dass21_asRepository = dass21_asRepository;
        this.credibilityRepository = credibilityRepository;
    }

    private void prepareQuestionnaireData(QuestionnaireData data) {
        ParticipantDAO participant = (ParticipantDAO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        data.setParticipantDAO(participant);
        data.setDate(new Date());
    }

    /** DASS 21
     * ---------**/
    @RequestMapping(value="DASS21_AS", method=RequestMethod.GET)
    public ModelAndView showContacts() {
        return new ModelAndView("questions/DASS21_AS", "DASS21_AS", new DASS21_AS());
    }

    @RequestMapping(value="DASS21_AS", method = RequestMethod.POST)
    String handleDASS21(@ModelAttribute("DASS21_AS") DASS21_AS dass21,
                        BindingResult result) {

        ParticipantDAO participant = (ParticipantDAO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        dass21_asRepository.save(dass21);
        return "/home";
    }

    /** Credibility
     * ---------**/
    @RequestMapping(value="credibility", method=RequestMethod.GET)
    public ModelAndView showCredibility() {
        return new ModelAndView("questions/credibility", "credibility", new DASS21_AS());
    }

    @RequestMapping(value="credibility", method = RequestMethod.POST)
    String handleCredibility(@ModelAttribute("credibility") Credibility credibility,
                        BindingResult result) {

        prepareQuestionnaireData(credibility);
        credibilityRepository.save(credibility);
        return "/home";
    }


}
