package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="Gidi")
@EqualsAndHashCode(callSuper = true)
@Data
public class Gidi extends LinkedQuestionnaireData {

    private String accepted;
    private String declined;

}

