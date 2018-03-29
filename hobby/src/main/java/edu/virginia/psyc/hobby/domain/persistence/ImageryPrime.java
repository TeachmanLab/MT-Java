package edu.virginia.psyc.hobby.domain.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

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

    private String situation;

}