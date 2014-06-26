package edu.virginia.psyc.pi.mvc;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
    private DemographicRepository demographicRepository;
    private ParticipantRepository participantRepository;

    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public QuestionController(DASS21_ASRepository dass21_asRepository,
                              CredibilityRepository credibilityRepository,
                              DemographicRepository demographicRepository,
                              ParticipantRepository participantRepository) {
        this.dass21_asRepository = dass21_asRepository;
        this.credibilityRepository = credibilityRepository;
        this.demographicRepository = demographicRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * Does some tasks common to all forms:
     *  - Adds the current session name to the data being recorded
     *  - Marks this "task" as complete, and moves the participant on to the next session
     *  - Connects the data to the participant who completed it.
     * @param data
     */
    private void recordSessionProgress(QuestionnaireData data) {

        ParticipantDAO dao      = (ParticipantDAO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Participant participant = participantRepository.entityToDomain(dao);

        // Record the session for which this questionnaire was completed.
        data.setSession(participant.getCurrentSession().getName());

        LOG.info("******************* RECORD SESSION PROGRESS CALLED");

        // Update the participant's session status, and save back to the database.
        participant.completeCurrentTask();
        participantRepository.domainToEntity(participant, dao);
        participantRepository.save(dao);

        // Connect the participant to the data being recorded.
        data.setParticipantDAO(dao);
        data.setDate(new Date());
    }

    /** DASS 21
     * ---------**/
    @RequestMapping(value="DASS21_AS", method=RequestMethod.GET)
    public ModelAndView showDASS21() {
        return new ModelAndView("questions/DASS21_AS", "DASS21_AS", new DASS21_AS());
    }

    @RequestMapping(value="DASS21_AS", method = RequestMethod.POST)
    RedirectView handleDASS21(@ModelAttribute("DASS21_AS") DASS21_AS dass21,
                        BindingResult result) {

        recordSessionProgress(dass21);
        dass21_asRepository.save(dass21);
        return new RedirectView("/session");
    }

    /** Credibility
     * ---------**/
    @RequestMapping(value="credibility", method=RequestMethod.GET)
    public ModelAndView showCredibility() {
        return new ModelAndView("questions/credibility", "credibility", new DASS21_AS());
    }

    @RequestMapping(value="credibility", method = RequestMethod.POST)
    RedirectView handleCredibility(@ModelAttribute("credibility") Credibility credibility,
                        BindingResult result) {

        recordSessionProgress(credibility);
        credibilityRepository.save(credibility);
        return new RedirectView("/session");
    }

    /** Demographics
     * ---------**/
    @RequestMapping(value="demographics", method=RequestMethod.GET)
    public String showDemographics() {
        return "/questions/demographics";
    }

    @RequestMapping(value="demographics", method = RequestMethod.POST)
    RedirectView handleDemographics(@ModelAttribute("demographics") Demographic demographic,
                             BindingResult result) {

        recordSessionProgress(demographic);
        demographicRepository.save(demographic);
        return new RedirectView("/session");
    }


}


