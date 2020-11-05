package edu.virginia.psyc.kaiser.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Table(name="Identity")
@EqualsAndHashCode(callSuper = true)
@Data
@Exportable(export = false)  // This is identifying information
public class Identity extends LinkedQuestionnaireData {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private Date dateOfBirth;

}

