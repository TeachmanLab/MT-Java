package edu.virginia.psyc.kaiser.controller;

import edu.virginia.psyc.kaiser.domain.KaiserStudy;
import org.mindtrails.controller.BaseController;
import org.mindtrails.domain.RestExceptions.NoConditionSpecifiedException;
import org.mindtrails.domain.RestExceptions.NoSuchConditionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
public class KaiserAccountController extends BaseController {

    @RequestMapping(value="kaiserStudyAccount/create", method = RequestMethod.GET)
    public String setCondition(HttpSession session,
                               final @RequestParam(value = "condition", required = true) String condition) {

        if (condition == null || condition.length() == 0) {
            throw new NoConditionSpecifiedException();
        }

        // Check if one of the six randomization conditions
        // Protects against someone trying to join study without being randomize
        // boolean isAllowableCondition = Arrays.stream(KaiserStudy.CONDITION.values()).anyMatch((t) -> t.name().equals(condition));
        boolean isAllowableCondition = KaiserStudy.conditionMappings.containsKey(condition);
        if (!isAllowableCondition) {
            throw new NoSuchConditionException();
        } else {

            // In case of Kaiser study, store condition as session attribute then
            // continue into regular account creation method in the AccountController
            session.setAttribute("condition", KaiserStudy.conditionMappings.get(condition));
            return "redirect:/account/create";
        }
    }
}


