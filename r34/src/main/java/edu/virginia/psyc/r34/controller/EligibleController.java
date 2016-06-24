package edu.virginia.psyc.r34.controller;

import org.mindtrails.domain.Participant;
import edu.virginia.psyc.r34.domain.Dass21FromPi;
import edu.virginia.psyc.r34.domain.PiParticipant;
import edu.virginia.psyc.r34.persistence.Questionnaire.DASS21_AS;
import edu.virginia.psyc.r34.persistence.Questionnaire.DASS21_ASRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Date;

// import jdk.internal.org.objectweb.asm.tree.analysis.Value;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class EligibleController {

    private static final Logger LOG = LoggerFactory.getLogger(EligibleController.class);

    // Eligibility form is saved to the session for retrieval when the user it found.
    private static final String DASS21_SESSION = "dass21";

    @Autowired
    private DASS21_ASRepository dass21_asRepository;


    @RequestMapping("public/eligibility")
    public String showEligibility(ModelMap model) {
        // Template will set a difference form action if this variable is set to true.
        model.addAttribute("eligibility",true);
        PiParticipant p = new PiParticipant();
        p.setTheme("blue");
        model.addAttribute("participant",p);

        return "questions/DASS21_AS";
    }

    @RequestMapping(value="public/eligibilityCheck", method = RequestMethod.POST)
    public String checkEligibility(@ModelAttribute("DASS21_AS") DASS21_AS dass21_as,
                                   ModelMap model, HttpSession session) {

        dass21_as.setSessionId(session.getId());
        dass21_as.setDate(new Date());
        dass21_asRepository.save(dass21_as);
        session.setAttribute("reference", "MindTrails");

        model.addAttribute("participant", new Participant());

        if(dass21_as.eligibleScore()) {
            return "invitation";
        } else {
            return "ineligible";
        }
    }

    // An external endpoint that bypasses the eligibility form, in the case
    // where the form is filled out on a remote site, and it's results are
    // added here.
    @RequestMapping(value="public/eligible", method = RequestMethod.POST)
    public String eligable(@ModelAttribute Dass21FromPi data,
                                   ModelMap model,
                                   HttpSession session) throws Exception {

        model.addAttribute("visiting", true);
        DASS21_AS dass21 = data.asDass21Object();
        return checkEligibility(dass21,model,session);
    }

}