package edu.virginia.psyc.kaiser.controller;

import edu.virginia.psyc.kaiser.domain.KaiserStudy;
import org.mindtrails.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
public class KaiserAccountController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(KaiserAccountController.class);

    @RequestMapping(path="/public/kaiser", method = RequestMethod.GET)
    public String setCondition(HttpSession session,
                               final @RequestParam(value = "condition", required = false) String condition) {

//        LOG.info("Yes, Dan you are getting here.");
//
        if (condition == null || condition.length() == 0) {
            return "noCondition";
        }

        // Check if one of the six randomization conditions
        // Protects against someone trying to join study without being randomize
        // boolean isAllowableCondition = Arrays.stream(KaiserStudy.CONDITION.values()).anyMatch((t) -> t.name().equals(condition));
        boolean isAllowableCondition = KaiserStudy.conditionMappings.containsKey(condition);
        if (!isAllowableCondition) {
            return "noCondition";
        } else {

            // In case of Kaiser study, store condition as session attribute then
            // continue into regular account creation method in the AccountController
            session.setAttribute("condition", KaiserStudy.conditionMappings.get(condition).name());
            LOG.info("Condition is " + session.getAttribute("condition"));
            return "redirect:/account/create";
        }
    }
}


