package edu.virginia.psyc.pi.persistence.Questionnaire;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by dan on 9/8/15.
 */
@Entity
@Table(name="ReasonsForEnding")
@EqualsAndHashCode(callSuper = true)
@Data
public class ReasonsForEnding  extends SecureQuestionnaireData {


    @ElementCollection
    @CollectionTable(name = "reasonsForEndingList", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "reason")
    List<String> reasons;
    boolean thoughtInControl;
    String pointInControl;
    String whyInControl;
    String otherWhyInControl;
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
    @CollectionTable(name = "reasonsForEndingLocation", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "location")
    List<String> location;
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

}


