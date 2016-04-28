package edu.virginia.psyc.pi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.QuestionnaireData;
import edu.virginia.psyc.pi.persistence.TaskLogDAO;
import edu.virginia.psyc.pi.service.ExportService;
import edu.virginia.psyc.pi.service.RsaEncyptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.io.StringWriter;
import java.security.Principal;
import java.util.Date;
import java.util.Map;

/**
 * Created by dan on 4/25/16.
 */
@Controller@RequestMapping("/forms")
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
        return ("/questions/" + formName);
    }


    @RequestMapping(value = "{form}", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView saveForm(@PathVariable("form") String formName,
                    WebRequest request) throws IOException {

        JpaRepository repository = exportService.getRepositoryForName(formName);
        try {
            QuestionnaireData data = (QuestionnaireData) exportService.getDomainType(formName).newInstance();
            recordSessionProgress(data);
            WebRequestDataBinder binder = new WebRequestDataBinder(data);
            binder.bind(request);
            repository.save(data);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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

        // Connect the participant to the data being recorded.
        data.setParticipantRSA(encryptService.encryptIfEnabled(dao.getId()));

        data.setDate(new Date());
    }


}
