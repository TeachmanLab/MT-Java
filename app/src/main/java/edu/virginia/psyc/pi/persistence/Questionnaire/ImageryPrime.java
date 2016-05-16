package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import edu.virginia.psyc.pi.domain.PiParticipant;
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
        PiParticipant participant;
        Map<String,Object> attributes = new HashMap<>();

        if(p instanceof PiParticipant) {
            participant = (PiParticipant)p;
            boolean notFirst = p.getStudy().getCurrentSession().getIndex() > 1;

            attributes.put("notFirst", notFirst);
            attributes.put("prime", participant.getPrime().toString());
            attributes.put("participant", p);
        }
        return attributes;
    }


}
