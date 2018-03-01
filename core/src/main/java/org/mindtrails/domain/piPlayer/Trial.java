package org.mindtrails.domain.piPlayer;

import lombok.Data;
import org.mindtrails.domain.data.Exportable;

import javax.persistence.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/10/14
 * Time: 10:28 PM
 * An Java Object representation of dataDAO returned from PIPlayer.  Here is a short example.  The results
 * come back as a JSON array of dataDAO, each element in that array is a TrialDAO object.
 * [
 *   { "log_serial":2,
 *     "trial_id":3,
 *     "name":"IAT",
 *     "responseHandle":"right",
 *     "latency":9255,
 *     "stimuliDAO":["White People"],
 *     "mediaDAO":["wm1_nc.jpg"],
 *     "dataDAO":
 *              {"score":0,
 *               "block":1,
 *               "left2":"Black People",
 *               "right2":"White People",
 *               "condition":"Black People/White People"
 *               }
 *    }
 * ]
 */
@Entity
@Table(name="trial")
@Data
@Exportable
public class Trial {

    @TableGenerator(name = "QUESTION_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "QUESTION_GEN")
    private long id;

    private int log_serial;
    private String trial_id;
    private String name;
    private String responseHandle;
    private String script;
    private String session;
    private int participantId;
    private int latency;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trial")
    private Collection<PiStimuli> piStimuli;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trial")
    private Collection<PiMedia> media;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trial")
    private Collection<PiData> data;

    @Override
    public String toString() {
        return "TrialDAO{" +
                "log_serial=" + log_serial +
                ", trial_id=" + trial_id +
                ", name='" + name + '\'' +
                ", responseHandle='" + responseHandle + '\'' +
                ", stimuliDAO=" + piStimuli +
                ", mediaDAO=" + media +
                ", dataDAO=" + data +
                '}';
    }

    public Trial() {}

    public Trial(TrialJson j) {
        this.setId(j.getId());
        this. setLog_serial(j.getLog_serial());
        this. setTrial_id(j.getTrial_id());
        this. setName(j.getName());
        this. setResponseHandle(j.getResponseHandle());
        this. setLatency(j.getLatency());
        this.setPiStimuli(j.getStimuli());
        this. setMedia(j.getMedia());
        this. setData(j.getData());
        this.setParticipantId(j.getParticipant());
        this.setSession(j.getSession());
        this.setScript(j.getScript());
    }

    public TrialJson toTrialJson() {
        TrialJson t = new TrialJson();
        t.setId(id);
        t.setLog_serial(log_serial);
        t.setTrial_id(trial_id);
        t.setName(name);
        t.setResponseHandle(responseHandle);
        t.setLatency(latency);
        t.setStimuli(getStimuliAsList());
        t.setMedia(getMediaAsList());
        t.setData(getDataAsMap());
        t.setParticipant(participantId);
        t.setSession(session);
        t.setScript(script);

        return t;
    }

    public List<String> getStimuliAsList() {
        List l = new ArrayList<String>();
        for(PiStimuli s : piStimuli) {
            l.add(s.getValue());
        }
        return l;
    }

    public void setstimuli(Collection<PiStimuli> piStimuli) {
        this.piStimuli = piStimuli;
    }

    public void setPiStimuli(List<String> stimuliIn) {
        this.piStimuli = new ArrayList<PiStimuli>();
        for(String s: stimuliIn) {
            this.piStimuli.add(new PiStimuli(s, this));
        }
    }


    public Collection<PiMedia> getMedia() {
        return media;
    }

    public List<String> getMediaAsList() {
        List l = new ArrayList<String>();
        for(PiMedia m : media) {
            l.add(m.getValue());
        }
        return l;
    }

    public void setMediaDAO(Collection<PiMedia> media) {
        this.media = media;
    }

    public void setMedia(List<String> mediaIn) {
        this.media = new ArrayList<PiMedia>();
        for(String s: mediaIn) {
            this.media.add(new PiMedia(s, this));
        }
    }

    public Collection<PiData> getDataDAO() {
        return data;
    }

    public Map<String,String> getDataAsMap() {
        Map<String,String> dataMap = new HashMap();
        for(PiData d : this.data) {
            dataMap.put(d.getKey(), d.getValue());
        }
        return dataMap;
    }

    public void setDataDAO(Collection<PiData> piData) {
        this.data = piData;
    }

    public void setData(Map<String, String> data) {
        this.data = new ArrayList<PiData>();
        for(String key : data.keySet()) {
            this.data.add(new PiData(key, data.get(key), this));
        }
    }


}
