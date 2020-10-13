package edu.virginia.psyc.kaiser.persistence;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.mindtrails.domain.Participant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * This table is loaded each time we need to assign individuals into conditions for the R01 study.  Changes
 * to the values in this table will update the algorithms that assign participants to particular conditions.
 * The latest record in this table will be used.  We keep track of previous settings and when they were applied.
 */
@Entity
@Table(name="ConditionAssignmentSettings")
@Data
public class ConditionAssignmentSettings {

    @Id
    @GenericGenerator(name = "SETTING_GEN", strategy = "org.mindtrails.persistence.MindtrailsIdGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "table_name", value = "ID_GEN"),
            @org.hibernate.annotations.Parameter(name = "value_column_name", value = "GEN_VAL"),
            @org.hibernate.annotations.Parameter(name = "segment_column_name", value = "GEN_NAME"),
            @org.hibernate.annotations.Parameter(name = "segment_value", value = "condition_settings")})
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SETTING_GEN")
    protected Long id;
    @NotNull
    protected Date lastModified;
    @NotNull
    protected Double attritionThreshold;

}
