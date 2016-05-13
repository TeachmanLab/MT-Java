package edu.virginia.psyc.pi.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 9/2/14
 * Time: 3:50 PM
 * For updating a participant.
 */
@Data
public class ParticipantUpdateForm {

    @Size(min=2, max=100, message="Please provide a name of at least 3 characters.")
    private String fullName;

    @Email
    @NotNull
    private String email;

    private boolean emailOptout = false;  // User required to receive no more emails.

    private String theme;

}
