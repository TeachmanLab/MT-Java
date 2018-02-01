package org.mindtrails.persistence;

import com.fasterxml.jackson.annotation.*;
import org.mindtrails.domain.BaseStudy;
import org.mindtrails.domain.DoNotDelete;
import org.mindtrails.domain.Exportable;
import lombok.Data;
import org.mindtrails.domain.Study;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dan on 3/24/16.
 * Data about a Participant that can be exported.
 */
@Data
@Entity
@Table(name="participant")
@Exportable
@DoNotDelete
public class PSLinkExportDAO {
    @Id private long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity=BaseStudy.class)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true) // otherwise first ref as POJO, others as id
    @JsonProperty(value = "study_id")
    protected Study study;

}
