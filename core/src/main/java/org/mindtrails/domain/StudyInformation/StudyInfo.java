package org.mindtrails.domain.StudyInformation;

import lombok.Data;
import org.mindtrails.domain.Study;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains detailed information about a study, but is stateless.  It would be
 * good to expand is to contain the full study definition, and split that off from the
 * Study object, which is participant specific.  But right now, this is just used
 * to return a response to the API that is sensible.
 */
@Data
public class StudyInfo {
    private String name;
    private List<SessionInfo> sessions = new ArrayList<>();

    public StudyInfo(){};

    public StudyInfo(Study study) {
        BeanUtils.copyProperties(study, this);
        this.setSessions(study.getSessions().stream().map(session -> new SessionInfo(session)).collect(Collectors.toList()));
    }
}

