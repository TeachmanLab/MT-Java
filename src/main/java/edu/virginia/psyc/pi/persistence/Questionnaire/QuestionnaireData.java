package edu.virginia.psyc.pi.persistence.Questionnaire;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.virginia.psyc.pi.domain.CBMStudy;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 5/26/14
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
@Data
public abstract class QuestionnaireData {

//    @Id
//    @GeneratedValue
//    protected long id;

    @TableGenerator(name = "QUESTION_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "QUESTION_GEN")
    protected Long id;


    // An encrypted link to the participant;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String participantRSA;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE, dd MMM yyyy HH:mm:ss Z", timezone="EST")
    protected Date date;
    protected String session;


    /** ==============================================================
     *       Some utility methods for exporting csv data from the forms
     *  ==============================================================
     */

    /**
     * Returns a list of all attributes
     */
    public List<String> listHeaders() {
        Method[] methods = this.getClass().getMethods();
        List<String> headers = new ArrayList<>();
        // Add in headers.
        for(Method method : methods) {
            if (isGetter(method)) {
                headers.add(method.getName().substring(3));
            }
        }
        return headers;
    }

    public void appendToCSV(StringBuffer csv) {
        Method[] methods = this.getClass().getMethods();
        ParticipantDAO participantDAO;
        CBMStudy.NAME session;
        List list;
        String data;

        for(Method method : methods){
            if(isGetter(method)) {
                try {
                    if(null == method.invoke(this)) {
                        data = "";
                    } else if(method.getReturnType().isPrimitive()) {
                        data = method.invoke(this).toString();
                    } else if (String.class.equals(method.getReturnType())) {
                        data = method.invoke(this).toString();
                    } else if (List.class.equals(method.getReturnType())) {
                        StringBuffer values = new StringBuffer();
                        list = (List)method.invoke(this);
                        for(int i = 0; i < list.size(); i++) {
                            values.append(list.get(i).toString());
                            if (i < list.size() -1) values.append("; ");
                        }
                        data = values.toString();
                    } else if (Date.class.equals(method.getReturnType())) {
                        data = method.invoke(this).toString();
                    } else if (ParticipantDAO.class.equals(method.getReturnType())) {
                        participantDAO = (ParticipantDAO)method.invoke(this);
                        data = "" + participantDAO.getId();
                    } else if (CBMStudy.NAME.class.equals(method.getReturnType())) {
                        session  = (CBMStudy.NAME)method.invoke(this);
                        data = session.toString();
                    } else if (Class.class.equals(method.getReturnType())) {
                        data = ((Class)method.invoke(this)).getSimpleName();
                    } else {
                        data = method.getReturnType().getName();
                    }
                    csv.append("\"");
                    csv.append(data.replaceAll("\"", "\\\""));
                    csv.append("\"");
                    csv.append(",");
                } catch (InvocationTargetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    public static boolean isGetter(Method method){
        if(!method.getName().startsWith("get"))       return false;
        if(method.getParameterTypes().length != 0)    return false;
        if(void.class.equals(method.getReturnType())) return false;
        return true;
    }





}
