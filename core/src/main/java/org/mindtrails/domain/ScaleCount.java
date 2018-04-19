package org.mindtrails.domain;

import lombok.Data;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.persistence.QuestionnaireRepository;
import org.mindtrails.persistence.TaskLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScaleCount {

    private ArrayList<countMap> pairs = new ArrayList<>();

    private String scaleName;
    private Integer uniqueIDCount;
    private List<Session> sessionList;
    private boolean hasDuplication;

    public ScaleCount(Study study, Scale scale, Integer countDistinct, TaskLogRepository taskLogRepository){
        this.scaleName = scale.getName();
        this.uniqueIDCount = countDistinct;
        this.sessionList = study.getSessions();

        this.pairs.add(new countMap("Unique Participant No.", this.uniqueIDCount));
        for (Session session: this.sessionList) {
            if (!session.getName().toLowerCase().equals("complete")) {
                this.pairs.add(new countMap(session.getName(),taskLogRepository.countDistinctBySessionNameAndAndTaskName(session.getName(),scale.getName())));
            }
        }

    }

}
