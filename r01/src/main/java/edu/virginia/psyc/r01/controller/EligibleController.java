package edu.virginia.psyc.r01.controller;

import edu.virginia.psyc.r01.persistence.ExpectancyBias;
import edu.virginia.psyc.r01.persistence.ExpectancyBiasRepository;
import org.mindtrails.controller.BaseController;
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


/**
* Verify that a new person is eligible for participating in the study.
 */
@Controller
public class EligibleController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(EligibleController.class);

    // Eligibility form is saved to the session for retrieval when the user it found.
    private static final String BIAS_SESSION = "bias";

    @Autowired
    private ExpectancyBiasRepository biasRepository;


    @RequestMapping("public/eligibility")
    public String showEligibility(ModelMap model) {
        // Template will set a difference form action if this variable is set to true.
        model.addAttribute("eligibility",true);
        return "questions/ExpectancyBias";
    }

    @RequestMapping(value="public/eligibilityCheck", method = RequestMethod.POST)
    public String checkEligibility(@ModelAttribute("ExpectancyBias") ExpectancyBias bias,
                                   ModelMap model, HttpSession session) {

        bias.setSessionId(session.getId());
        bias.setDate(new Date());
        biasRepository.save(bias);

        if(bias.eligible()) {
            return "invitation";
        } else {
            return "ineligible";
        }
    }


}