package org.mindtrails.domain.questionnaire;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections.map.HashedMap;
import org.hibernate.annotations.GenericGenerator;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.Participant;
import lombok.Data;
import org.mindtrails.domain.hasParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created a secure Questionnaire with an ecnrypted link to the Participant.
 */
@MappedSuperclass
@Data
@Exportable // All Quetionnaire data should be exportable.
public abstract class QuestionnaireData implements hasParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionnaireData.class);

    @Id
    @GenericGenerator(name = "QUESTION_GEN", strategy = "org.mindtrails.persistence.MindtrailsIdGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "table_name", value = "ID_GEN"),
            @org.hibernate.annotations.Parameter(name = "value_column_name", value = "GEN_VAL"),
            @org.hibernate.annotations.Parameter(name = "segment_column_name", value = "GEN_NAME"),
            @org.hibernate.annotations.Parameter(name = "segment_value", value = "question") })
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "QUESTION_GEN")
    protected Long id;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE, dd MMM yyyy HH:mm:ss Z", timezone="EST")
    @NotNull
    protected Date date;
    protected String session;
    protected String tag; // Optional additional data defined by a task definition

    @NotNull
    protected double timeOnPage; // Time spend on page in seconds.

    @JsonIgnore
    @Transient
    protected Map<String,String> validationErrors = new HashMap<>();

    /**
     * Provides a list of measures grouped by measures with the same scale.  Often there is just one scale,
     * so all measures are returned as a part of a single group with a blank name.
     * @return
     */
    public Collection<MeasureGroup> getGroups() {
        Map<String, MeasureGroup> groups = new TreeMap<>();
        MeasureGroup group;
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(MeasureField.class)) {
                MeasureField mf = field.getAnnotation(MeasureField.class);
                Measure m = new Measure(field.getName(), mf.desc());
                if(validationErrors.containsKey(field.getName())) {
                    m.setError(true);
                    m.setErrorMessage(validationErrors.get(field.getName()));
                }
                if(!groups.containsKey(mf.group())) {
                    group = new MeasureGroup(mf.group(), getDescription(mf.group()), getScale(mf.group()));

                    groups.put(mf.group(), group);
                }
                groups.get(mf.group()).measures.add(m);
            }
        }
        return groups.values();
    }

    /**
     * Override to set descriptions for each group.
     * @return
     */
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        return desc;
    }

    private String getDescription(String group) {
        Map<String,String> descriptions = getGroupDescriptions();
        if(descriptions.containsKey(group)) {
            return descriptions.get(group);
        } else {
            return "";
        }
    }

    /**
     * Override this to provide the complete scale for all possible answers in a measure.  Maybe
     * specific to a particular group, or it may return the same scale for all groups.
     * @return
     */
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new HashedMap();
        return tmpScale;
    }

    /**
     * Provides validation against all measures.  Once called, the "getMeasures" function will include
     * error information.
     * @param validator
     * @return
     */
    public boolean validate(Validator validator) {
        Set<? extends ConstraintViolation<?>> violations = validator.validate(this);
        for(ConstraintViolation v : violations) {
            validationErrors.put(v.getPropertyPath().toString(), v.getMessage());
            LOG.info(v.getPropertyPath() + " :: " + v.getMessage());
        }
        return validationErrors.isEmpty();
    }

    /**
     * Override this method to add custom information that should
     * be passed through to your html web form before it is displayed.
     * @param p Participant.
     */
    public Map<String,Object> modelAttributes(Participant p) {
        return new HashMap<>();
    }

}
