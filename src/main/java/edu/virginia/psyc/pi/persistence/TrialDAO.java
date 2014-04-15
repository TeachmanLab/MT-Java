package edu.virginia.psyc.pi.persistence;

import edu.virginia.psyc.pi.rest.json.TrialJson;

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
public class TrialDAO {

    @Id @GeneratedValue private long id;
    private int log_serial;
    private String trial_id;
    private String name;
    private String responseHandle;
    private int latency;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<StimuliDAO> stimuliDAO;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<MediaDAO> mediaDAO;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<DataDAO> dataDAO;

    @Override
    public String toString() {
        return "TrialDAO{" +
                "log_serial=" + log_serial +
                ", trial_id=" + trial_id +
                ", name='" + name + '\'' +
                ", responseHandle='" + responseHandle + '\'' +
                ", stimuliDAO=" + stimuliDAO +
                ", mediaDAO=" + mediaDAO +
                ", dataDAO=" + dataDAO +
                '}';
    }

    public TrialDAO() {}

    public TrialDAO(TrialJson j) {
        this.setId(j.getId());
        this. setLog_serial(j.getLog_serial());
        this. setTrial_id(j.getTrial_id());
        this. setName(j.getName());
        this. setResponseHandle(j.getResponseHandle());
        this. setLatency(j.getLatency());
        this. setStimuli(j.getStimuli());
        this. setMedia(j.getMedia());
        this. setData(j.getData());
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
        return t;
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLog_serial() {
        return log_serial;
    }

    public void setLog_serial(int log_serial) {
        this.log_serial = log_serial;
    }

    public String getTrial_id() {
        return trial_id;
    }

    public void setTrial_id(String trial_id) {
        this.trial_id = trial_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponseHandle() {
        return responseHandle;
    }

    public void setResponseHandle(String responseHandle) {
        this.responseHandle = responseHandle;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public Collection<StimuliDAO> getStimuliDAO() {
        return stimuliDAO;
    }

    public List<String> getStimuliAsList() {
        List l = new ArrayList<String>();
        for(StimuliDAO s : stimuliDAO) {
            l.add(s.getValue());
        }
        return l;
    }

    public void setStimuliDAO(Collection<StimuliDAO> stimuliDAO) {
        this.stimuliDAO = stimuliDAO;
    }

    public void setStimuli(List<String> stimuliIn) {
        this.stimuliDAO = new ArrayList<StimuliDAO>();
        for(String s: stimuliIn) {
            this.stimuliDAO.add(new StimuliDAO(s));
        }
    }


    public Collection<MediaDAO> getMediaDAO() {
        return mediaDAO;
    }

    public List<String> getMediaAsList() {
        List l = new ArrayList<String>();
        for(MediaDAO m : mediaDAO) {
            l.add(m.getValue());
        }
        return l;
    }

    public void setMediaDAO(Collection<MediaDAO> mediaDAO) {
        this.mediaDAO = mediaDAO;
    }

    public void setMedia(List<String> mediaIn) {
        this.mediaDAO = new ArrayList<MediaDAO>();
        for(String s: mediaIn) {
            this.mediaDAO.add(new MediaDAO(s));
        }
    }


    public Collection<DataDAO> getDataDAO() {
        return dataDAO;
    }

    public Map<String,String> getDataAsMap() {
        Map<String,String> dataMap = new HashMap();
        for(DataDAO d : this.dataDAO) {
            dataMap.put(d.getKey(), d.getValue());
        }
        return dataMap;
    }

    public void setDataDAO(Collection<DataDAO> dataDAO) {
        this.dataDAO = dataDAO;
    }

    public void setData(Map<String, String> data) {
        this.dataDAO = new ArrayList<DataDAO>();
        for(String key : data.keySet()) {
            this.dataDAO.add(new DataDAO(key, data.get(key)));
        }
    }


}
