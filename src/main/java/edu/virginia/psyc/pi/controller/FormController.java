package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.RestExceptions.NoModelForFormException;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.QuestionnaireData;
import edu.virginia.psyc.pi.persistence.Questionnaire.LinkedQuestionnaireData;
import edu.virginia.psyc.pi.persistence.Questionnaire.SecureQuestionnaireData;
import edu.virginia.psyc.pi.persistence.TaskLogDAO;
import edu.virginia.psyc.pi.service.ExportService;
import edu.virginia.psyc.pi.service.RsaEncyptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Date;

/**
 * Created by dan on 4/25/16.
 */
@Controller@RequestMapping("/questions")
public class FormController extends BaseController {
    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(FormController.class);

    @Autowired
    private RsaEncyptionService encryptService;

    @Autowired
    private ExportService exportService;


    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public FormController(ParticipantRepository repository) {
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
            LOG.error("Recieved a post for form '" + formName +"' But no Repository exists with this name.");
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
