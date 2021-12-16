package org.mindtrails.controller;

import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.StudyInformation.SessionInfo;
import org.mindtrails.domain.StudyInformation.StudyInfo;
import org.mindtrails.domain.StudyInformation.TaskInfo;
import org.mindtrails.service.ExportService;
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

    private final ParticipantService participantService;

    private final ExportService exportService;

    @Autowired
    public StudyInfoController(ParticipantService participantService, ExportService exportService) {
        this.participantService = participantService;
        this.exportService = exportService;
    }

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody List<StudyInfo> getStudies() {
        List<StudyInfo> studyInfos =
            participantService.getStudies().stream().map(s -> new StudyInfo(s)).collect(Collectors.toList());
        updateDeleteableFlag(studyInfos);
        return (studyInfos);
    }

    /**
     * Churns through the list of studiess and checks for a deletable flag on all the tasks.
     * @param studyInfos
     */
    private void updateDeleteableFlag(List<StudyInfo> studyInfos) {
        for (StudyInfo s : studyInfos) {
            for (SessionInfo si : s.getSessions()) {
                for (TaskInfo taskInfo : si.getTasks()) {
                    taskInfo.setDeleteable(true);
                    Class<?> domainType = exportService.getDomainType(taskInfo.getName(), false);
                    if (domainType != null) {
                        if (domainType.isAnnotationPresent(DoNotDelete.class)) {
                            taskInfo.setDeleteable(false);
                        }
                    }
                }
            }
        }
    }

}
