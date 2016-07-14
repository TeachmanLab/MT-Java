package edu.virginia.psyc.r34.persistence.Questionnaire;

import edu.virginia.psyc.r34.domain.CBMStudy;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import edu.virginia.psyc.r34.domain.R34Participant;
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
public class ImageryPrime extends SecureQuestionnaireData {

    private String prime;  // Either 'ANXIOUS' or 'NEUTRAL'
    private String situation;


    @Override
    public Map<String,Object> modelAttributes(Participant p) {
        R34Participant participant;
        Map<String,Object> attributes = new HashMap<>();

        if(p instanceof R34Participant) {
            participant = (R34Participant)p;
            boolean notFirst = p.getStudy().getCurrentSession().getName() != CBMStudy.NAME.SESSION1.toString();

            attributes.put("notFirst", notFirst);
            attributes.put("prime", participant.getPrime().toString());
            attributes.put("participant", p);
        }
        return attributes;
    }


}
