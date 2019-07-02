package org.mindtrails.domain.Action;

import lombok.Data;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.data.Exportable;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Action")
@Data
@Exportable
@DoNotDelete
public class Action extends LinkedQuestionnaireData {
    private String name;
    private Date timestamp;
}




