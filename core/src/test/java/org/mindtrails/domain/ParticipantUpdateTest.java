package org.mindtrails.domain;

import org.junit.Before;
import org.junit.Test;
import org.mindtrails.domain.forms.ParticipantUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.Assert.*;
/**
 * Created by dan on 2/10/17.
 */
public class ParticipantUpdateTest {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantUpdate.class);

    private Validator validator;

    private ParticipantUpdate participantUpdate;

    @Before
    public void init() {

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();

        this.participantUpdate = new ParticipantUpdate();
        this.participantUpdate.setFullName("Dan");
        this.participantUpdate.setEmail("Daniel.h.funk@gmail.com");

    }

    @Test
    public void phoneCanBeNull() {
        this.participantUpdate.setPhone(null);
        Set<ConstraintViolation<ParticipantUpdate>> violations = this.validator.validate(this.participantUpdate);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void phoneFormattedCorrectly() {
        String vp = "+15404570024";
        assertEquals(vp, this.participantUpdate.formatPhone("(540) 457.0024"));
        assertEquals(vp, this.participantUpdate.formatPhone("(540)457-0024"));
        assertEquals(vp, this.participantUpdate.formatPhone("540-457-0024"));
        assertEquals(vp, this.participantUpdate.formatPhone("5404570024"));
        assertEquals(vp, this.participantUpdate.formatPhone("1-5404570024"));
    }

    @Test
    public void validatePhoneNumberWhenBadFormat() {
        this.participantUpdate.setPhone("5");
        Set<ConstraintViolation<ParticipantUpdate>> violations = this.validator.validate(this.participantUpdate);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatePhoneNumberWhenPlusAnd10Digits() {
        this.participantUpdate.setPhone("+15404570024");
        Set<ConstraintViolation<ParticipantUpdate>> violations = this.validator.validate(this.participantUpdate);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void validatePhoneNumberWhenParensAndDashes() {
        this.participantUpdate.setPhone("(540) 457-0024");
        Set<ConstraintViolation<ParticipantUpdate>> violations = this.validator.validate(this.participantUpdate);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void validatePhoneNumberWhenSlightMistake() {
        this.participantUpdate.setPhone("(540) 457-024");
        Set<ConstraintViolation<ParticipantUpdate>> violations = this.validator.validate(this.participantUpdate);
        assertFalse(violations.isEmpty());
    }


}


