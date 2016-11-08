package org.mindtrails.controller;

import org.mindtrails.domain.StudyInfo;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dan on 11/4/16.
 * An API Endpoint that provides detailed information about the the Study
 */
@Controller
@RequestMapping("/api/study")
public class StudyInfoController {

    private ParticipantService participantService;

    @Autowired
    public StudyInfoController(ParticipantService service) {
        this.participantService = service;
    }

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody List<StudyInfo> getStudies() {
        List<StudyInfo> studyInfos =
            participantService.getStudies().stream().map(s -> new StudyInfo(s)).collect(Collectors.toList());
        return (studyInfos);
    }


}
