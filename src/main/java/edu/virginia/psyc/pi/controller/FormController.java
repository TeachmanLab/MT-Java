package edu.virginia.psyc.pi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.forms.FormDAO;
import edu.virginia.psyc.pi.persistence.forms.FormRepository;
import edu.virginia.psyc.pi.service.RsaEncyptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;
import java.security.Principal;
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
    private FormRepository formRepository;


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
    String saveForm(ModelMap model, Principal principal, @PathVariable("form") String formName,
                    @RequestParam Map<String,String> allRequestParams) throws IOException {

        // Jsonify the form parameters
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter swriter = new StringWriter();
        objectMapper.writeValue(swriter, allRequestParams);
        String json = swriter.toString();

        // Persist the data
        Participant p = getParticipant(principal);
        String rsa    = encryptService.encryptIfEnabled(p.getId());
        FormDAO form  = new FormDAO(rsa, p.getStudy().getCurrentSession().getName(), json);
        formRepository.save(form);
        System.out.println("Received Data:" + json);
        return ("/");
    }

    @RequestMapping(value = {"/search/", "/search"}, method = RequestMethod.GET)
    public String search(
            @RequestParam Map<String,String> allRequestParams, ModelMap model) {
        return "viewName";
    }


}
