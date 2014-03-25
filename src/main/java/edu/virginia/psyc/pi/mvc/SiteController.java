package edu.virginia.psyc.pi.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/24/14
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/")
public class SiteController {

    private static final Logger LOG = LoggerFactory.getLogger(SiteController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String getCurrentMenu(Model model) {
        LOG.debug("Yummy MenuItemDetails to home view");
        model.addAttribute("username","Dan");
        return "home";
    }

}
