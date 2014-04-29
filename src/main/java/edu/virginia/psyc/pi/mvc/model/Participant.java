package edu.virginia.psyc.pi.mvc.model;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 4/29/14
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class Participant {

    private long id;

    @Size(min=2, max=100)
    private String fullName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private boolean admin;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
