package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="Consent")
@EqualsAndHashCode(callSuper = true)
@Data
public class Consent extends SecureQuestionnaireData {
    private boolean Agree;
}

