package edu.virginia.psyc.pi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/10/14
 * Time: 10:28 PM
 * An Java Object representation of data returned from PIPlayer.  Here is a short example.  The results
 * come back as a JSON array of data, each element in that array is a Trial object.
 * [
 *   { "log_serial":2,
 *     "trial_id":3,
 *     "name":"IAT",
 *     "responseHandle":"right",
 *     "latency":9255,
 *     "stimuli":["White People"],
 *     "media":["wm1_nc.jpg"],
 *     "data":
 *              {"score":0,
 *               "block":1,
 *               "left2":"Black People",
 *               "right2":"White People",
 *               "condition":"Black People/White People"
 *               }
 *    }
 * ]
 */
public class Trial {

    private int id;
    private int log_serial;
    private int trial_id;
    private String name;
    private String responseHandle;
    private int latency;
    private Collection<String> stimuli = new ArrayList<String>();
    private Collection<String> media   = new ArrayList<String>();
    private Map<String,Object> data    = new HashMap<String, Object>();

    @Override
    public String toString() {
        return "Trial{" +
                "log_serial=" + log_serial +
                ", trial_id=" + trial_id +
                ", name='" + name + '\'' +
                ", responseHandle='" + responseHandle + '\'' +
                ", stimuli=" + stimuli +
                ", media=" + media +
                ", data=" + data +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLog_serial() {
        return log_serial;
    }

    public void setLog_serial(int log_serial) {
        this.log_serial = log_serial;
    }

    public int getTrial_id() {
        return trial_id;
    }

    public void setTrial_id(int trial_id) {
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

    public Collection<String> getStimuli() {
        return stimuli;
    }

    public void setStimuli(Collection<String> stimuli) {
        this.stimuli = stimuli;
    }

    public Collection<String> getMedia() {
        return media;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public void setMedia(Collection<String> media) {
        this.media = media;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void addData(String key, Object value) {
        System.out.println("Adding " + key + ":" + value);
        this.data.put(key,value);
    }

}
