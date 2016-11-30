package org.mindtrails.domain.StudyInformation;


import lombok.Data;
import org.mindtrails.domain.Task;
import org.springframework.beans.BeanUtils;

@Data
public class TaskInfo {
    private String name;
    private String displayName;
    private Task.TYPE type;
    private int duration;
    private boolean deleteable;

    public TaskInfo() {}

    public TaskInfo(Task task) {
        BeanUtils.copyProperties(task, this);
    }
}
