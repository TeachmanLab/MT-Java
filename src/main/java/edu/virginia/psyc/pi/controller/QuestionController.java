package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/questionsOld")
public class QuestionController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    @Autowired private FollowUp_ChangeInTreatment_Repository followup_Repository;
    @Autowired private ImpactAnxiousImagery_Repository impact_Repository;
    @Autowired private MentalHealthHxTxRepository mh_Repository;
    @Autowired private MultiUserExperienceRepository mue_Repository;
    @Autowired private PilotUserExperienceRepository pue_Repository;
    @Autowired private DemographicRepository demographicRepository;
    @Autowired private ImageryPrimeRepository imageryPrimeRepository;
    @Autowired private RR_Repository rr_repository;
    @Autowired private CCRepository cc_repository;
    @Autowired private OARepository oa_repository;
    @Autowired private DDRepository dd_repository;
    @Autowired private DD_FURepository dd_fu_repository;
    @Autowired private BBSIQRepository bbsiqRepository;
    @Autowired private AnxietyTriggersRepository anxietyTriggersRepository;
    @Autowired private SUDSRepository sudsRepository;
    @Autowired private VividRepository vividRepository;
    @Autowired private ReasonsForEndingRepository reasonsForEndingRepository;
    @Autowired private CIHSRepository cihsRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RsaEncyptionService encryptService;

    @Autowired
    private ExportService exportService;

    @Autowired
    public QuestionController(ParticipantRepository repository) {
        this.participantRepository   = repository;
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

        ParticipantDAO dao = (ParticipantDAO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        dao = participantRepository.findByEmail(dao.getEmail()); // Refresh session object from database.
        Participant participant = participantRepository.entityToDomain(dao);

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
//        data.setParticipantRSA(encryptService.encryptIfEnabled(dao.getId()));

        data.setDate(new Date());
    }

    private ModelAndView modelAndView(Principal principal, String url, String name, Object model) {
           Map<String,Object> models = new HashMap<>();
           models.put("participant", getParticipant(principal));
           models.put(name, model);
           return new ModelAndView(url, models);
    }


    /**
     * ImageryPrime
     * This can be either an AIP (Anxious Imagery Prime) or NIP (Neutral Imagery Prime) depending
     * on the status of the participant.
     * ---------*
     */

    @RequestMapping(value = "ImageryPrime", method = RequestMethod.GET)
    public ModelAndView showIP(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        boolean notFirst = p.getStudy().getCurrentSession().getIndex() > 1;
        model.addAttribute("notFirst", notFirst);
        model.addAttribute("prime", p.getPrime().toString());
        return modelAndView(principal, "/questions/ImageryPrime", "IP", new ImageryPrime());
    }

    @RequestMapping(value = "ImageryPrime", method = RequestMethod.POST)
    RedirectView handleIP(@ModelAttribute("IP") ImageryPrime prime,
                              BindingResult result) {
        recordSessionProgress(prime);
        imageryPrimeRepository.save(prime);
        return new RedirectView("/session/next");
    }


    /**
     * Demographics
     * ---------*
     */
    @RequestMapping(value = "demographics", method = RequestMethod.GET)
    public ModelAndView showDemographics(Principal principal) {
        return modelAndView(principal, "/questions/demographics", "demographics", new Demographic());
    }

    @RequestMapping(value = "demographics", method = RequestMethod.POST)
    RedirectView handleDemographics(@ModelAttribute("demographics") Demographic demographic,
                                    BindingResult result) {

        recordSessionProgress(demographic);
        demographicRepository.save(demographic);
        return new RedirectView("/session/next");
    }



    /**
     * RR
     * ---------*
     */
    @RequestMapping(value = "RR", method = RequestMethod.GET)
    public ModelAndView showRR(Principal principal) {
        return modelAndView(principal, "/questions/RR", "RR", new RR());
    }

    @RequestMapping(value = "RR", method = RequestMethod.POST)
    RedirectView handleRR(@ModelAttribute("RR") RR rr,
                                 BindingResult result) {

        recordSessionProgress(rr);
        rr_repository.save(rr);
        return new RedirectView("/session/next");
    }



    /**
     * CC
     * ---------*
     */
    @RequestMapping(value = "CC", method = RequestMethod.GET)
    public ModelAndView showCC(Principal principal) {
        return modelAndView(principal, "/questions/CC", "CC", new CC());
    }

    @RequestMapping(value = "CC", method = RequestMethod.POST)
    RedirectView handleRR(@ModelAttribute("CC") CC cc,
                          BindingResult result) {

        recordSessionProgress(cc);
        cc_repository.save(cc);
        return new RedirectView("/session/next");
    }



    /**
     * OA
     * ---------*
     */
    @RequestMapping(value = "OA", method = RequestMethod.GET)
    public ModelAndView showOA(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("inSessions", p.inSession());
        return modelAndView(principal, "/questions/OA", "OA", new OA());
    }

    @RequestMapping(value = "OA", method = RequestMethod.POST)
    RedirectView handleOA(@ModelAttribute("OA") OA oa,
                          BindingResult result, Principal principal) throws MessagingException{

        // Connect this object to the Participant, as we will need to reference it later.
        ParticipantDAO dao      = getParticipantDAO(principal);
        Participant participant = participantRepository.entityToDomain(dao);
        oa.setParticipantDAO(dao);
        recordSessionProgress(oa);
        oa_repository.save(oa);

        // If the users score differs from there original score and places the user
        // "at-risk", then send a message to the administrator.
        List<OA> previous = oa_repository.findByParticipantDAO(oa.getParticipantDAO());
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
     * Daily Drinking
     * ---------*
     */
    @RequestMapping(value = "DD", method = RequestMethod.GET)
    public ModelAndView showDD(Principal principal) {
        return modelAndView(principal, "/questions/DD", "DD", new DD());
    }

    @RequestMapping(value = "DD", method = RequestMethod.POST)
    RedirectView handleDD(@ModelAttribute("DD") DD dd,
                          BindingResult result) {

        recordSessionProgress(dd);
        dd_repository.save(dd);
        return new RedirectView("/session/next");
    }


    /**
     * Daily Drinking Follow up
     * ---------*
     */
    @RequestMapping(value = "DD_FU", method = RequestMethod.GET)
    public ModelAndView showDDFU(Principal principal) {
        return modelAndView(principal, "/questions/DD_FU", "DD_FU", new DD());
    }

    @RequestMapping(value = "DD_FU", method = RequestMethod.POST)
    RedirectView handleDD(@ModelAttribute("DD_FU") DD_FU dd_fu,
                          BindingResult result) {

        recordSessionProgress(dd_fu);
        dd_fu_repository.save(dd_fu);
        return new RedirectView("/session/next");
    }



    /**
     * BBSIQ
     * ---------*
     */
    @RequestMapping(value = "BBSIQ", method = RequestMethod.GET)
    public ModelAndView showBBSIQ(Principal principal) {
        return modelAndView(principal, "/questions/BBSIQ", "BBSIQ", new BBSIQ());
    }

    @RequestMapping(value = "BBSIQ", method = RequestMethod.POST)
    RedirectView handleDD(@ModelAttribute("BBSIQ") BBSIQ bbsiq,
                          BindingResult result) {

        recordSessionProgress(bbsiq);
        bbsiqRepository.save(bbsiq);
        return new RedirectView("/session/next");
    }


    /**
     * Anxiety Triggers
     * ---------*
     */
    @RequestMapping(value = "AnxietyTriggers", method = RequestMethod.GET)
    public ModelAndView showAnxietyTriggers(Principal principal) {
        return modelAndView(principal, "/questions/AnxietyTriggers", "AnxietyTriggers", new AnxietyTriggers());
    }

    @RequestMapping(value = "AnxietyTriggers", method = RequestMethod.POST)
    RedirectView handleAnxietyTriggers(@ModelAttribute("AnxietyTriggers") AnxietyTriggers triggers,
                            BindingResult result) {

        recordSessionProgress(triggers);
        anxietyTriggersRepository.save(triggers);
        return new RedirectView("/session/next");
    }



    /**
     * Reasons For Ending Study
     * ---------*
     */
    @RequestMapping(value = "ReasonsForEnding", method = RequestMethod.GET)
    public ModelAndView showReasonsForEnding(Principal principal) {
        return modelAndView(principal, "/questions/ReasonsForEnding", "ReasonsForEnding", new ReasonsForEnding());
    }

    @RequestMapping(value = "ReasonsForEnding", method = RequestMethod.POST)
    String handleReasonsForEnding(@ModelAttribute("ReasonsForEnding") ReasonsForEnding reasons,
                                       BindingResult result) {

        recordSessionProgress(reasons);
        reasonsForEndingRepository.save(reasons);
        return "debriefing";
    }



}

