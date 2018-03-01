package edu.virginia.psyc.r34.persistence.Questionnaire;

import edu.virginia.psyc.r34.domain.R34Study;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="ImageryPrime")
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageryPrime extends LinkedQuestionnaireData {

    private String prime;  // Either 'ANXIOUS' or 'NEUTRAL'
    private String situation;


    @Override
    public Map<String,Object> modelAttributes(Participant p) {
        Participant participant;
        R34Study study;
        Map<String,Object> attributes = new HashMap<>();

        if(p.getStudy() instanceof R34Study) {
            study = (R34Study)p.getStudy();
            boolean notFirst = p.getStudy().getCurrentSession().getName() != R34Study.NAME.SESSION1.toString();

            attributes.put("notFirst", notFirst);
            attributes.put("prime", study.getPrime().toString());
            attributes.put("participant", p);
        }
        return attributes;
    }


}
