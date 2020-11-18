package edu.virginia.psyc.kaiser.controller;


import edu.virginia.psyc.kaiser.persistence.ConditionAssignmentSettings;
import edu.virginia.psyc.kaiser.persistence.ConditionAssignmentSettingsRepository;
import edu.virginia.psyc.kaiser.service.KaiserParticipantService;
import org.mindtrails.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("admin/settings")
public class KaiserSettingsController {

    @Autowired
    private ConditionAssignmentSettingsRepository settingsRepository;

    @Autowired
    private KaiserParticipantService participantService;

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


