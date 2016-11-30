package org.mindtrails.domain.StudyInformation;


import lombok.Data;
import org.mindtrails.domain.Session;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SessionInfo {
    private String name;
    private String displayName;
    private List<TaskInfo> tasks = new ArrayList<>();

    public SessionInfo() {}

    public SessionInfo(Session session) {
        BeanUtils.copyProperties(session, this);
        this.setTasks(session.getTasks().stream().map(task -> new TaskInfo(task)).collect(Collectors.toList()));
    }
}
