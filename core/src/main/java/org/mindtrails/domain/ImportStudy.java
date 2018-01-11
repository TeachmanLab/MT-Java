package org.mindtrails.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

public class ImportStudy extends BaseStudy {
    @Override
    public List<Session> getStatelessSessions() {
        return null;
    }

    @Override
    @Transient
    public String getName() {
        return null;
    }

    @Override
    public int getCurrentTaskIndex() {
        return 0;
    }

    @Override
    public Date getLastSessionDate() {
        return null;
    }

    @Override

    public void setLastSessionDate(Date date) {

    }

    @Override
    public String getConditioning() {
        return null;
    }

    @Override
    @Transient
    @JsonIgnore
    public List<String> getConditions() {
        return null;
    }
}
