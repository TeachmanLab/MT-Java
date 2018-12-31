package org.mindtrails.controller;

import org.mindtrails.domain.ExportMode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.NoModelForFormException;
import org.mindtrails.domain.RestExceptions.WrongFormException;
import org.mindtrails.domain.questionnaire.HasReturnDate;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.QuestionnaireData;
import org.mindtrails.service.ExportService;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Validator;
import java.security.Principal;
import java.util.Date;

/**
 * Handles Form postings from Questionnaires. Expects the following:
 * 1. An Html Form, whose form action = "questions/FormName"
 * 2. An Entity class that describes how the form data should be stored.
 * 3. A Repository Class that allows the entity to be saved to the database.
 *
 * If these things exist on the class path, then any data posted to the form
 * will to saved to the database and accessible through the export routine.
 * You do not need to add anything to this class for that to work.
 *
 * If you need custom data provided to your form, or if you need to perform
 * a custom action after the form is submitted, you can add your own endpoint
 * to this class.  use "recordSessionProgress" method to record your data
 * if you override the submission behavior.
 *
 * */
@Controller@RequestMapping("/questions")
public class QuestionController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private ExportService exportService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private Validator validator;


    @RequestMapping(value = "{form}", method = RequestMethod.GET)
    public String showForm(ModelMap model, Principal principal, @PathVariable("form") String formName) {

        Participant participant = participantService.get(principal);

        // It is possible that the Quesionnaire Data object will want to add some additional
        // parameters to the web form.
        try {
            if(exportService.getDomainType(formName, false) == null) {
                String message = "You are missing a model for storing data for the form " +
                        "'" + formName + "'";
                LOG.error(message);
                throw new RuntimeException(message);
            }
            QuestionnaireData data = (QuestionnaireData) exportService.getDomainType(formName, false).newInstance();
            model.addAllAttributes(data.modelAttributes(participant));
            model.addAttribute("model", data);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ("questions/" + formName);
    }


    @ExportMode
    @RequestMapping(value = "{form}", method = RequestMethod.POST)
    public String handleForm(@PathVariable("form") String formName,
                             WebRequest request, ModelMap model, Principal principal,
                             Device device,
                             @RequestHeader(value="User-Agent", defaultValue="foo") String userAgent) throws Exception {
        return saveForm(formName, request, model, principal, userAgent, device);
   }


    /**
     * Handles a form submission in a standardized way.  This is useful if
     * you extend this class to handle a custom form submission.
     */
    @ExportMode
    public String saveForm(String formName, WebRequest request, ModelMap model, Principal principal, String userAgent,
                           Device device)  {

        JpaRepository repository = exportService.getRepositoryForName(formName, false);
        if(repository == null) {
            LOG.error("Received a post for form '" + formName +"' But no Repository exists with this name.");
            throw new NoModelForFormException();
        }
        try {
            Participant participant = participantService.get(principal);
            Class<?> questionClass = exportService.getDomainType(formName, false);
            QuestionnaireData data = (QuestionnaireData)questionClass.newInstance();
            WebRequestDataBinder binder = new WebRequestDataBinder(data, exportService.getDomainType(formName, false).getName());
            binder.bind(request);
            data.setDate(new Date());
            setReturnDate(participant, data);
            if(data.validate(this.validator)) {
                recordSessionProgress(formName, data, device, userAgent);
                repository.save(data);
                return "redirect:/session/next";
            } else {
                model.addAttribute("error", "Please complete all required fields.");
                model.addAllAttributes(data.modelAttributes(participant));
                model.addAttribute("model", data);
                return ("questions/" + formName);
            }
        } catch (ClassCastException | InstantiationException | IllegalAccessException e) {
            LOG.error("Failed to save model '" + formName + "' : " + e.getMessage());
            throw new NoModelForFormException(e);
        }
    }

    /**
     * If a questionniare implements the hasReturnDate, we should record this on the particpant
     * model, and include an email invitation in the followup email messages.
     */
    private void setReturnDate(Participant participant, QuestionnaireData data) {
        if(data instanceof HasReturnDate) {
            HasReturnDate inviteData = (HasReturnDate)data;
            participant.setReturnDate(inviteData.getReturnDate());
            participantService.save(participant);
        }

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
    @ExportMode
    protected void recordSessionProgress(String formName, QuestionnaireData data, Device device, String userAgent) {

        Participant participant;

        participant = (Participant) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(participantService.findByEmail(participant.getEmail()) != null)
            participant = participantService.findByEmail(participant.getEmail()); // Refresh session object from database.

        // Only treat this as progress in the session if the submitted task is
        // a part of the regularly occurring questionnaires.  Other questions, such
        // as a "reason for ending" should not be recorded as making progress in the session.
        boolean isProgress = participant.getStudy().isProgress(formName);

        String currentTaskName = participant.getStudy().getCurrentSession().getCurrentTask().getName();
        if(!currentTaskName.equals(formName) && isProgress && !participant.isAdmin()) {
            String error = "The current task for this participant is : " + currentTaskName + " however, they submitted the form:" + formName;
            LOG.info(error);
            throw new WrongFormException(error);
        }

        // Grab the tag of the current task, and incorporate it into the data.
        String tag = participant.getStudy().getCurrentSession().getCurrentTask().getTag();
        data.setTag(tag);

        // Save time on Task to TaskLog.
        double timeOnTask = data.getTimeOnPage();

        if(timeOnTask == 0d) throw new RuntimeException("Missing Time on Page, please add it.");

        // Attempt to set the participant link, depending on sub-class type
        if(data instanceof LinkedQuestionnaireData)
            ((LinkedQuestionnaireData) data).setParticipant(participant);


        data.setSession(participant.getStudy().getCurrentSession().getName());

        // Update the participant's session status, and save back to the database.
        if(isProgress) {
            participant.getStudy().completeCurrentTask(timeOnTask, device, userAgent);
            participantService.save(participant);
        }
    }


}
