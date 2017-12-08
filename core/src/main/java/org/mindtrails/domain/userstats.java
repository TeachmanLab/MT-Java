package org.mindtrails.domain;

import lombok.Data;

import java.util.*;

import org.joda.time.DateTime;
import org.mindtrails.domain.BaseStudy;
import org.mindtrails.domain.Session;

/**
 * Created by any on 9/29/17.
 */
@Data
public class userstats {
    private Map<String, Integer> sessionMap = new HashMap<String, Integer>();
    private List<Session> sessionList;
    private List<String> conditionList;
    private Map<String, Integer> activeMap = new HashMap<String, Integer>();
    private Map<String, Integer> inactiveMap = new HashMap<String, Integer>();
    private Map<String, Integer> compMap = new HashMap<String, Integer>();
    private int participantNum;
    private Map<Long,StudyStats> studyMap= new HashMap<Long,StudyStats>();
    private Map<Date,Integer> loginMap=new TreeMap<Date, Integer>();
    private Map<Key,Integer> conditionSessionMap=new HashMap<Key,Integer>();
    private Map<Key,Integer> conditionSessionActiveMap=new HashMap<Key,Integer>();
    private Map<Key,Integer> conditionSessionInActiveMap=new HashMap<Key,Integer>();
    private Map<Key,Integer> conditionSessionCompMap=new HashMap<Key,Integer>();
    //private Map<String,Integer>conditionMap=new HashMap<String,Integer>();

    @Data
    public static class Key{
        private String sessionName;
        private String condition;
        public Key(String sessionName, String condition) {
            this.sessionName = sessionName;
            this.condition = condition;
        }

    }

    public userstats(List<ParticipantStats> participants,List<StudyStats> studystats,Study study) {
        this.participantNum = participants.size();
        this.sessionList = study.getSessions();
        this.conditionList=study.getConditions();

        int res = this.participantNum;
        Date[] weeks=new Date[25];
        weeks[24]=DateTime.now().toDate();
        for (Session session : this.sessionList) {
            this.sessionMap.put(session.getName(), 0);
            this.activeMap.put(session.getName(), 0);
            this.inactiveMap.put(session.getName(), 0);
            this.compMap.put(session.getName(), 0);
            for (String condition: this.conditionList){
                Key csCondition=new Key(session.getName(),condition);
                this.conditionSessionMap.put(csCondition,0);
                this.conditionSessionActiveMap.put(csCondition,0);
                this.conditionSessionInActiveMap.put(csCondition,0);
                this.conditionSessionCompMap.put(csCondition,0);
            }
        }
        for(StudyStats s: studystats){
            studyMap.put(s.getId(),s);
        }
        for (int i=24;i>0;i--){
            weeks[24-i]= DateTime.now().minusDays(7*i).toDate();
            this.loginMap.put(weeks[24-i],0);
        }


        for (ParticipantStats p : participants) {
            for (int i=1;i<=24;i++){
                if (p.getLastLoginDate()!=null && p.getLastLoginDate().before(weeks[i])){
                    this.loginMap.put(weeks[i-1],this.loginMap.get(weeks[i-1]) + 1);
                    break;
                }

            }


            String currentSessionName = studyMap.get(p.getId()).getCurrentSession();
            String conditioning=studyMap.get(p.getId()).getConditioning();

            this.sessionMap.put(currentSessionName, this.sessionMap.get(currentSessionName) + 1);
            Key csCondition=new Key(currentSessionName,conditioning);
            this.conditionSessionMap.put(csCondition,this.conditionSessionMap.get(csCondition)+1);


            if (p.isActive()) {
                this.activeMap.put(currentSessionName, this.activeMap.get(currentSessionName) + 1);
                this.conditionSessionActiveMap.put(csCondition,this.conditionSessionActiveMap.get(csCondition)+1);
            } else {

                this.inactiveMap.put(currentSessionName, this.inactiveMap.get(currentSessionName) + 1);
                this.conditionSessionInActiveMap.put(csCondition,this.conditionSessionInActiveMap.get(csCondition)+1);


            }


        }
        for (Session session : this.sessionList) {
            res = res - this.sessionMap.get(session.getName());
            this.compMap.put(session.getName(), res);

        }
    }




}










