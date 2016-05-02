package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.RestExceptions.NoModelForFormException;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.*;
import edu.virginia.psyc.pi.persistence.TaskLogDAO;
import edu.virginia.psyc.pi.service.EmailService;
import edu.virginia.psyc.pi.service.ExportService;
import edu.virginia.psyc.pi.service.RsaEncyptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Handles Form postings from Questionnaires. Expects the following:
 * 1. An Html Form, whose form action = "questions/FormName"
 * 2. An Entity class that describes how the form data should be stored.
 * 3. A Repository Class that allows the entity to be saved to the database.
 *
 * If these things exist on the class path, then any data posted to the form
 * will to saved to the datbase and accessible through the export routine.
 * You do not need to add anything to this class for that to work.
 *
 * If you need custom data provided to your form, or if you need to peform
 * a custom action after the form is submitted, you can add your own endpoint
 * to this class.  use "recordSessionProgress" method to record your data
 * if you override the submission behavior.
 *
 * */
@Controller@RequestMapping("/questions")
public class QuestionController extends BaseController {
    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private RsaEncyptionService encryptService;

    @Autowired
    private ExportService exportService;

    @Autowired
    private ImageryPrimeRepository imageryPrimeRepository;

    @Autowired
    private OARepository oaRepository;

    @Autowired
    private EmailService emailService;

    /**
     * ImageryPrime
     * This can be either an AIP (Anxious Imagery Prime) or NIP (Neutral Imagery Prime) depending
     * on the status of the participant.
     * ---------*
     */

    @RequestMapping(value = "ImageryPrime", method = RequestMethod.GET)
    public String showIP(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        boolean notFirst = p.getStudy().getCurrentSession().getIndex() > 1;
        model.addAttribute("notFirst", notFirst);
        model.addAttribute("prime", p.getPrime().toString());
        model.addAttribute("participant", p);
        return ("/questions/ImageryPrime");
    }


    /**
     * OA
     * ---------*
     */
    @RequestMapping(value = "OA", method = RequestMethod.GET)
    public String showOA(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("inSessions", p.inSession());
        model.addAttribute("participant", getParticipant(principal));
        return ("/questions/OA");
    }

    @RequestMapping(value = "OA", method = RequestMethod.POST)
    RedirectView handleOA(@ModelAttribute("OA") OA oa,
                          BindingResult result, Principal principal) throws MessagingException {

        // Connect this object to the Participant, as we will need to reference it later.
        ParticipantDAO dao      = getParticipantDAO(principal);
        Participant participant = participantRepository.entityToDomain(dao);
        oa.setParticipantDAO(dao);
        recordSessionProgress(oa);
        oaRepository.save(oa);

        // If the users score differs from there original score and places the user
        // "at-risk", then send a message to the administrator.
        List<OA> previous = oaRepository.findByParticipantDAO(oa.getParticipantDAO());
        OA firstEntry = Collections.min(previous);

        if(oa.atRisk(firstEntry)) {
            if(!participant.isIncrease30()) { // alert admin the first time.
                emailService.sendAtRiskAdminEmail(participant, firstEntry, oa);
                dao.setIncrease30(true);
                participantRepository.save(dao);
            }
            return new RedirectView("/session/atRisk");
        }
        return new RedirectView("/session/next");
    }


    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public QuestionController(ParticipantRepository repository) {
        this.participantRepository   = repository;
    }

    @RequestMapping(value = "{form}", method = RequestMethod.GET)
    public String showForm(ModelMap model, Principal principal, @PathVariable("form") String formName) {
        model.addAttribute("participant", getParticipant(principal));
        return ("questions/" + formName);
    }


    @RequestMapping(value = "{form}", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView saveForm(@PathVariable("form") String formName,
                    WebRequest request) throws Exception {

        JpaRepository repository = exportService.getRepositoryForName(formName);
        if(repository == null) {
            LOG.error("Received a post for form '" + formName +"' But no Repository exists with this name.");
            throw new NoModelForFormException();
        }
        try {
            QuestionnaireData data = (QuestionnaireData) exportService.getDomainType(formName).newInstance();
            recordSessionProgress(data);
            WebRequestDataBinder binder = new WebRequestDataBinder(data);
            binder.bind(request);
            repository.save(data);
        } catch (ClassCastException | InstantiationException | IllegalAccessException e) {
            LOG.error("Failed to save model '" + formName + "' : " + e.getMessage());
            throw new NoModelForFormException(e);
        }
        return new RedirectView("/session/next");
   }

    /**
     * Does some tasks common to all forms:
     * - Adds the current CBMStudy.NAME to the data being recorded
     * - Marks this "task" as complete, and moves the participant on to the next session
     * - Connects the data to the participant who completed it.
     * - Notifies the backup service that it may need to export data.
     *
     * @param data
     */
    private void recordSessionProgress(QuestionnaireData data) {

        Participant participant;
        ParticipantDAO dao;

        dao = (ParticipantDAO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(participantRepository.findByEmail(dao.getEmail()) != null)
            dao = participantRepository.findByEmail(dao.getEmail()); // Refresh session object from database.

        // Attempt to set the participant link, depending on sub-class type
        if(data instanceof LinkedQuestionnaireData)
            ((LinkedQuestionnaireData) data).setParticipantDAO(dao);
        if(data instanceof SecureQuestionnaireData)
            ((SecureQuestionnaireData) data).setParticipantRSA(encryptService.encryptIfEnabled(dao.getId()));


        participant = participantRepository.entityToDomain(dao);

        // Record the session for which this questionnaire was completed.
        data.setSession(participant.getStudy().getCurrentSession().getName());

        // Log the completion of the task
        TaskLogDAO taskDao = new TaskLogDAO(dao, participant.getStudy().getCurrentSession().getName(),
                participant.getStudy().getCurrentSession().getCurrentTask().getName());
        dao.addTaskLog(taskDao);

        // Update the participant's session status, and save back to the database.
        participant.getStudy().completeCurrentTask();
        participantRepository.domainToEntity(participant, dao);
        participantRepository.save(dao);

        data.setDate(new Date());
    }


}
