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
     * Provides a list of measures that can be used to automatically generate a form.
     * @return
     */
    public List<Measure> getMeasures() {
        List<Measure> measures = new ArrayList<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(MeasureField.class)) {
                MeasureField mf = field.getAnnotation(MeasureField.class);
                Measure m = new Measure(field.getName(), mf.desc(), mf.group(), this.getScale());
                if(validationErrors.containsKey(field.getName())) {
                    m.setError(true);
                    m.setErrorMessage(validationErrors.get(field.getName()));
                }
                measures.add(m);
            }
        }
        return measures;
    }

    /**
     * Returns all measures in a given group.
     * @return
     */
    public List<Measure> getMeasures(String group) {
        List<Measure> allMeasures = this.getMeasures();
        List<Measure> measures = new ArrayList<>();
        for(Measure m : allMeasures) {
            if (m.group.equals(group)) {
                measures.add(m);
            }
        }
        return measures;
    }



    /**
     * Override this to set descriptions for group names.
     * @param groupName
     * @return
     */
    public String getGroupDescription(String groupName) {
        return "";
    }

    /**
     * Override this to provide the complete scale for all possible answers in a measure.
     * @return
     */
    public Map<Integer, String> getScale() {
        Map<Integer, String> tmpScale = new HashedMap();
        return tmpScale;
    }

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
