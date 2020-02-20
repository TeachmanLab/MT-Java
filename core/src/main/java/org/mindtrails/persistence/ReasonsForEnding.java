package org.mindtrails.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by dan on 9/8/15.
 */
@Entity
@Table(name="ReasonsForEnding")
@EqualsAndHashCode(callSuper = true)
@Data
public class ReasonsForEnding extends LinkedQuestionnaireData {

    @ElementCollection
    @CollectionTable(name= "reasonsForEnding_deviceUse", joinColumns = @JoinColumn(name="id"))
    @Column(name = "deviceUse")
    List<String> deviceUse;

    @ElementCollection
    @CollectionTable(name = "reasonsForEnding_Reasons", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "reason")
    List<String> reasons;
    String End_Other_Desc;

    boolean thoughtInControl;
    String pointInControl="N/A";
    String whyInControl;
    String otherWhyInControl;
    String control_Desc;
    int helpful;
    int work;
    int easy;
    int interest;
    int inGeneral;
    int looked;
    int privacy;
    int understandAssessments;
    int understandTraining;
    int trust;
    @ElementCollection
    @CollectionTable(name = "reasonsForEnding_Location", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "location")
    List<String> location;
    String location_Desc;
    int focused;
    int internet;
    int connected;
    int forgot;
    int personalIssues;
    int navigationHard;
    int hardToUnderstand;
    int notUseful;
    int takeTooLong;
    int tooManyWords;
    int hardToRead;
    @ElementCollection
    @CollectionTable(name = "reasonsForEndingChangeMed", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "changeMed")
    List<String> changeMed;
    String changeMed_Desc;

}


