package edu.virginia.psyc.pi.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Stores email addresses of people who would like to be notified when the next
 * study is available.
 */
@Entity
@Table(name="notify_me")
@Data
public class NotifyDAO {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @Email
    @NotNull
    private String email;

}
