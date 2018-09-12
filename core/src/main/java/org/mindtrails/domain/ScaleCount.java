package org.mindtrails.domain;

import lombok.Data;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.QuestionnaireData;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.persistence.JsPsychRepository;
import org.mindtrails.persistence.QuestionnaireRepository;
import org.mindtrails.persistence.TaskLogRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScaleCount {

    private ArrayList<countMap> pairs = new ArrayList<>();

    private String scaleName;
    private Long uniqueIDCount;
    private List<Session> sessionList;
    private boolean hasDuplication;

    public ScaleCount(Study study, Scale scale, List<Participant> realAccount, JpaRepository rep){

        this.scaleName = scale.getName();
        this.sessionList = study.getSessions();


        List<Study> realStudies = realAccount.stream()
                .map(Participant::getStudy).collect(Collectors.toList());
        List<Long> realID = realAccount.stream().map(Participant::getId).collect(Collectors.toList());

        if (QuestionnaireRepository.class.isAssignableFrom(rep.getClass())) {
            HashSet<Long> uniqueID = new HashSet<Long>();
            ((QuestionnaireRepository) rep).findDistinctByParticipantIn(realAccount).stream().forEach(q -> uniqueID.add(((LinkedQuestionnaireData) q).getParticipant().getId()));
            this.uniqueIDCount = Long.valueOf(uniqueID.size());
            this.pairs.add(new countMap("Unique Participant No.", Long.valueOf(this.uniqueIDCount), Long.valueOf(0)));
            for (Session session : this.sessionList) {
                if (!session.getName().toLowerCase().equals("complete")) {
                    List<LinkedQuestionnaireData> content = ((QuestionnaireRepository) rep).findDistinctByParticipantInAndSession(realAccount, session.getName());
                    Long everything = Long.valueOf(content.size());
                    HashSet<Long> uniqueBySession = new HashSet<>();
                    content.stream().forEach(q -> uniqueBySession.add(q.getParticipant().getId()));
                    Long duplicated = everything - uniqueBySession.size();
                    this.pairs.add(new countMap(session.getName(), Long.valueOf(uniqueBySession.size()), duplicated));
                }
            }
        }
        if (this.uniqueIDCount == null) {
            this.uniqueIDCount = Long.valueOf(-1);
        };

    }

}
