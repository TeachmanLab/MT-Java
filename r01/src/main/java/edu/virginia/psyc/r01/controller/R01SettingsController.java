package edu.virginia.psyc.r01.controller;


import edu.virginia.psyc.r01.persistence.ConditionAssignmentSettings;
import edu.virginia.psyc.r01.persistence.ConditionAssignmentSettingsRepository;
import edu.virginia.psyc.r01.service.R01ParticipantService;
import org.mindtrails.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("admin/settings")
public class R01SettingsController {

    @Autowired
    private ConditionAssignmentSettingsRepository settingsRepository;

    @Autowired
    private R01ParticipantService participantService;

    @Autowired
    private ImportService importService;

    @RequestMapping(method = RequestMethod.GET)
    public String showSettings(ModelMap modelMap) {
        List<ConditionAssignmentSettings> settings = settingsRepository.findAllByOrderByLastModifiedDesc();
        modelMap.addAttribute("settings", settings);
        modelMap.addAttribute("default", participantService.defaultThreshold);
        modelMap.addAttribute("isExportMode", importService.isExporting());
        return("admin/settings");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String newSettings(ModelMap model, @ModelAttribute ConditionAssignmentSettings settings) {
        settings.setLastModified(new Date());
        this.settingsRepository.save(settings);
        return showSettings(model);
    }

}


