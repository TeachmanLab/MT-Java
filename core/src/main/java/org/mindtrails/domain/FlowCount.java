package org.mindtrails.domain;

import com.sun.tools.javac.comp.Flow;
import lombok.Data;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FlowCount {

    private ArrayList<countMap> pairs = new ArrayList<countMap>();
    private Integer accessAttempt;
    private Integer passAttempt;
    //These will change according to condition or gender.
    private Integer totalAccount;
    private Integer ittCount;
    private List<Session> sessionList;


    public FlowCount(Study study, VisitRepository visitRepository, ParticipantRepository participantRepository, JsPsychRepository jsPsychRepository,TaskLogRepository taskLogRepository){
        this.accessAttempt = visitRepository.findAll().size();
        this.passAttempt = this.accessAttempt - visitRepository.findAllByNameEndsWith("_not_pass").size();
        this.totalAccount = participantRepository.findAll().size();
        this.ittCount = jsPsychRepository.countDistinctByParticipant();

        this.pairs.add(new countMap("Assessed for eligibility",this.accessAttempt));
        this.pairs.add(new countMap("Eligible", this.passAttempt));
        this.pairs.add(new countMap("Created an account", this.totalAccount));
        this.pairs.add(new countMap("Intent to treat",this.ittCount));
        this.sessionList = study.getSessions();
        for (Session session: this.sessionList) {
            if (!session.getName().toLowerCase().equals("complete")) {
                this.pairs.add(new countMap("Completed " + session.getName(), taskLogRepository.countDistinctBySessionNameAndAndTaskName(session.getName(), "SESSION_COMPLETE")));
            }
        }
    }

}
