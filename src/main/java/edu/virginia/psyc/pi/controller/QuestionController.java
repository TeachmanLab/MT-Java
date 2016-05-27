package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.RestExceptions.WrongFormException;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.*;
import edu.virginia.psyc.pi.persistence.TaskLogDAO;
import edu.virginia.psyc.pi.service.EmailService;
import edu.virginia.psyc.pi.service.RsaEncyptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping("/questions")
public class QuestionController extends BaseController {

    @Autowired private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    private static final String BY_PASS_SESSION_CHECK = "BY_PASS_CHECK";

    @Autowired private DASS21_ASRepository dass21_asRepository;
    @Autowired private DASS21_DSRepository dass21_dsRepository;
    @Autowired private QOLRepository qol_Repository;
    @Autowired private AUDIT_Repository audit_Repository;
    @Autowired private FollowUp_ChangeInTreatment_Repository followup_Repository;
    @Autowired private ImpactAnxiousImagery_Repository impact_Repository;
    @Autowired private MentalHealthHxTxRepository mh_Repository;
    @Autowired private MultiUserExperienceRepository mue_Repository;
    @Autowired private PilotUserExperienceRepository pue_Repository;
    @Autowired private CredibilityRepository credibilityRepository;
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
    private void recordSessionProgress(String formName, QuestionnaireData data) {


        ParticipantDAO dao = (ParticipantDAO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        dao = participantRepository.findByEmail(dao.getEmail()); // Refresh session object from database.
        Participant participant = participantRepository.entityToDomain(dao);

        // If the data submitted, isn't the data the user should be completeing right now,
        // thown an exception and prevent them from moving forward.
        String currentTaskName = participant.getStudy().getCurrentSession().getCurrentTask().getName();
        if(!currentTaskName.equals(formName) && !formName.equals(BY_PASS_SESSION_CHECK)) {
            LOG.info("The current task for this participant is : " + currentTaskName + " however, they submitted the form:" + formName);
            throw new WrongFormException();
        }
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

    private ModelAndView modelAndView(Principal principal, String url, String name, Object model) {
           Map<String,Object> models = new HashMap<>();
           models.put("participant", getParticipant(principal));
           models.put(name, model);
           return new ModelAndView(url, models);
    }

    /**
     * DASS 21 AS
     * ---------*
     */
    @RequestMapping(value = "DASS21_AS", method = RequestMethod.GET)
    public ModelAndView showDASS21_AS(Principal principal) {
        return modelAndView(principal, "/questions/DASS21_AS", "DASS21_AS", new DASS21_AS());
    }

    @RequestMapping(value = "DASS21_AS", method = RequestMethod.POST)
    RedirectView handleDASS21_AS(@ModelAttribute("DASS21_AS") DASS21_AS dass21_as,
                                 BindingResult result, Principal principal) throws MessagingException {

        // Connect this object to the Participant, as we will need to reference it later.
        dass21_as.setParticipantDAO(getParticipantDAO(principal));
        recordSessionProgress("DASS21_AS",dass21_as);
        dass21_asRepository.save(dass21_as);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "DASS21_AS/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportDASS21_AS() {
        return(objectListToCSV(dass21_asRepository.findAll()));
    }


    /**
     * DASS 21 DS
     * ---------*
     */
    @RequestMapping(value = "DASS21_DS", method = RequestMethod.GET)
    public ModelAndView showDASS21_DS(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("inSessions", p.inSession());
        model.addAttribute("p", p);
        return modelAndView(principal, "/questions/DASS21_DS", "DASS21_DS", new DASS21_DS());
    }

    @RequestMapping(value = "DASS21_DS", method = RequestMethod.POST)
    RedirectView handleDASS21_DS(@ModelAttribute("DASS21_DS") DASS21_DS dass21_ds,
                                 BindingResult result) {

        recordSessionProgress("DASS21_DS",dass21_ds);
        dass21_dsRepository.save(dass21_ds);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "DASS21_DS/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportDASS21_DS() {
        return(objectListToCSV(dass21_dsRepository.findAll()));
    }



    /**
     * QOL
     * ---------*
     */
    @RequestMapping(value = "QOL", method = RequestMethod.GET)
    public ModelAndView showqol(Principal principal) {
        return modelAndView(principal, "questions/QOL", "QOL", new QOL());
    }

    @RequestMapping(value = "QOL", method = RequestMethod.POST)
    RedirectView handleqol(@ModelAttribute("qol") QOL qol,
                           BindingResult result) {

        recordSessionProgress("QOL",qol);
        qol_Repository.save(qol);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "QOL/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportQOL() {
        return(objectListToCSV(qol_Repository.findAll()));
    }


    /**
     * AUDIT
     * ---------*
     */
    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public ModelAndView showaudit(Principal principal) {
        return modelAndView(principal, "/questions/audit", "aduit", new AUDIT());
    }

    @RequestMapping(value = "audit", method = RequestMethod.POST)
    RedirectView handleaudit(@ModelAttribute("audit") AUDIT audit,
                             BindingResult result) {

        recordSessionProgress("audit",audit);
        audit_Repository.save(audit);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "audit/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportAudit() {
        return(objectListToCSV(audit_Repository.findAll()));
    }


    /**
     * Credibility
     * ---------*
     */
    @RequestMapping(value = "/credibility", method = RequestMethod.GET)
    public ModelAndView showCredibility(Principal principal) {
        return modelAndView(principal, "/questions/credibility", "credibility", new Credibility());
    }

    @RequestMapping(value = "/credibility", method = RequestMethod.POST)
    RedirectView handleCredibility(@ModelAttribute("credibility") Credibility credibility,
                                   BindingResult result) {

        recordSessionProgress("credibility",credibility);
        credibilityRepository.save(credibility);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "credibility/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportCredibility() {
        return(objectListToCSV(credibilityRepository.findAll()));
    }


    /**
     * FollowUp_ChangeInTreatment
     * ---------*
     */

    @RequestMapping(value = "FU", method = RequestMethod.GET)
    public ModelAndView showFollowUp(Principal principal) {
        return modelAndView(principal, "/questions/FU", "FU", new FollowUp_ChangeInTreatment());
    }

    @RequestMapping(value = "FU", method = RequestMethod.POST)
    RedirectView handleFollowUp(@ModelAttribute("FU") FollowUp_ChangeInTreatment followup,
                                BindingResult result) {

        recordSessionProgress("FU", followup);
        followup_Repository.save(followup);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "FU/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportFu() {
        return(objectListToCSV(followup_Repository.findAll()));
    }

    /**
     * MentalHealthHxTx
     * ---------*
     */

    @RequestMapping(value = "MH", method = RequestMethod.GET)
    public ModelAndView showMentalHealthHxTx(Principal principal) {
        return modelAndView(principal, "/questions/MH", "MH", new MentalHealthHxTx());
    }

    @RequestMapping(value = "MH", method = RequestMethod.POST)
    RedirectView handleMentalHealthHxTx(@ModelAttribute("MH") MentalHealthHxTx mh,
                                        BindingResult result) {

        recordSessionProgress("MH",mh);
        mh_Repository.save(mh);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "MH/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportMentalHealthHxTx() {
        return(objectListToCSV(mh_Repository.findAll()));
    }

    /**
     * Change in Help Seeking
     * -------------*
     */

    @RequestMapping(value="CIHS", method = RequestMethod.GET)
    public  ModelAndView showCIHS (Principal principal) {
        return modelAndView(principal, "/questions/CIHS", "CIHS", new CIHS());
    }

    @RequestMapping(value = "CIHS", method = RequestMethod.POST)
    RedirectView handleCIHS(@ModelAttribute("CIHS") CIHS cihs,
                            BindingResult result) {
        recordSessionProgress("CIHS",cihs);
        cihsRepository.save(cihs);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "CIHS/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.y
    String exportCIHS() {return(objectListToCSV(cihsRepository.findAll())); }

    /**
     * MultiUserExperience
     * ---------*
     */

    @RequestMapping(value = "MUE", method = RequestMethod.GET)
    public ModelAndView showMultiUserExperience(Principal principal) {
        return modelAndView(principal, "/questions/MUE", "MUE", new MultiUserExperience());
    }

    @RequestMapping(value = "MUE", method = RequestMethod.POST)
    RedirectView handleMultiUserExperience(@ModelAttribute("MUE") MultiUserExperience mue,
                                           BindingResult result) {

        recordSessionProgress("MUE",mue);
        mue_Repository.save(mue);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "MUE/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportMUE() {
        return(objectListToCSV(mue_Repository.findAll()));
    }


    /**
     * PilotUserExperience
     * ---------*
     */

    @RequestMapping(value = "PUE", method = RequestMethod.GET)
    public ModelAndView showPilotUserExperience(Principal principal) {
        return modelAndView(principal, "questions/PUE", "PUE", new PilotUserExperience());
    }

    @RequestMapping(value = "PUE", method = RequestMethod.POST)
    RedirectView handlePilotUserExperience(@ModelAttribute("PUE") PilotUserExperience pue,
                                           BindingResult result) {

        recordSessionProgress("PUE",pue);
        pue_Repository.save(pue);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "PUE/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportPUE() {
        return(objectListToCSV(pue_Repository.findAll()));
    }

/**
 * SUDS
 * -----*
 *
 */
@RequestMapping(value = "SUDS", method = RequestMethod.GET)
public ModelAndView showSUDS(Principal principal) {
    return modelAndView(principal, "questions/SUDS", "SUDS", new SUDS());
}

    @RequestMapping(value = "SUDS", method = RequestMethod.POST)
    RedirectView handleSUDS(@ModelAttribute("SUDS") SUDS suds,
                                           BindingResult result) {

        recordSessionProgress("SUDS",suds);
        sudsRepository.save(suds);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "SUDS/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportSUDS() {
        return(objectListToCSV(sudsRepository.findAll()));
    }

    /**
     * Vividness
     * -----*
     *
     */
    @RequestMapping(value = "Vivid", method = RequestMethod.GET)
    public ModelAndView showVivid(Principal principal) {
        return modelAndView(principal, "questions/Vivid", "Vivid", new Vivid());
    }

    @RequestMapping(value = "Vivid", method = RequestMethod.POST)
    RedirectView handleVivid(@ModelAttribute("Vivid") Vivid vivid,
                            BindingResult result) {

        recordSessionProgress("Vivid",vivid);
        vividRepository.save(vivid);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "Vivid/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportVivid() {
        return(objectListToCSV(vividRepository.findAll()));
    }

    /**
     * ImpactAnxiousImagery
     * ---------*
     */

    @RequestMapping(value = "Impact", method = RequestMethod.GET)
    public ModelAndView showImpact(Principal principal) {
        return modelAndView(principal, "/questions/Impact", "Impact", new ImpactAnxiousImagery());
    }

    @RequestMapping(value = "Impact", method = RequestMethod.POST)
    RedirectView handleImpact(@ModelAttribute("Impact") ImpactAnxiousImagery impact,
                              BindingResult result) {

        recordSessionProgress("Impact",impact);
        impact_Repository.save(impact);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "Impact/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportImplact() {
        return(objectListToCSV(impact_Repository.findAll()));
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
        recordSessionProgress("ImageryPrime",prime);
        imageryPrimeRepository.save(prime);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "ImageryPrime/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportIP() {
        return(objectListToCSV(imageryPrimeRepository.findAll()));
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

        recordSessionProgress("demographics",demographic);
        demographicRepository.save(demographic);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "demographics/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportDemographics() {
        return(objectListToCSV(demographicRepository.findAll()));
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

        recordSessionProgress("RR",rr);
        rr_repository.save(rr);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "RR/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportRR() {
        return(objectListToCSV(rr_repository.findAll()));
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

        recordSessionProgress("CC",cc);
        cc_repository.save(cc);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "CC/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportCC() {
        return(objectListToCSV(cc_repository.findAll()));
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
        recordSessionProgress("OA",oa);
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

    @RequestMapping(value = "OA/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportOA() {
        return(objectListToCSV(oa_repository.findAll()));
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

        recordSessionProgress("DD",dd);
        dd_repository.save(dd);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "DD/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportDD() {
        return(objectListToCSV(dd_repository.findAll()));
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

        recordSessionProgress("DD_FU", dd_fu);
        dd_fu_repository.save(dd_fu);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "DD_FU/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportDDFU() {
        return(objectListToCSV(dd_fu_repository.findAll()));
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

        recordSessionProgress("BBSIQ",bbsiq);
        bbsiqRepository.save(bbsiq);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "BBSIQ/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportBBSIQ() {
        return(objectListToCSV(bbsiqRepository.findAll()));
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

        recordSessionProgress("AnxietyTriggers", triggers);
        anxietyTriggersRepository.save(triggers);
        return new RedirectView("/session/next");
    }

    @RequestMapping(value = "AnxietyTriggers/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportAnxietyTriggers() {
        return(objectListToCSV(anxietyTriggersRepository.findAll()));
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

        recordSessionProgress(BY_PASS_SESSION_CHECK,reasons);
        reasonsForEndingRepository.save(reasons);
        return "debriefing";
    }

    @RequestMapping(value = "ReasonsForEnding/export", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody // Return the string directly, the return value is not a template name.
    String exportReasonsForEnding() {
        return(objectListToCSV(reasonsForEndingRepository.findAll()));
    }

    /**
     * Converts a list of objects into a string suitable for returning
     * as a csv.
     * @param objects
     * @return
     */
    public static String objectListToCSV(List<? extends QuestionnaireData> objects) {
        StringBuffer csv = new StringBuffer();

        if(objects.size() < 1) return "";

        List<String> headers = objects.get(0).listHeaders();

        // Add in headers.
        for (String header : headers) {
            csv.append("\"");
            csv.append(header);
            csv.append("\"");
            csv.append(",");
        }

        csv.append("\n");
        for(QuestionnaireData data : objects) {
            data.appendToCSV(csv);
            csv.append("\n");
        }
        return csv.toString();
    }

}

