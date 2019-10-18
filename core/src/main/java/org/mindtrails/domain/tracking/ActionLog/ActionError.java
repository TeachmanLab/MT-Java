package org.mindtrails.domain.tracking.ActionLog;

import lombok.Getter;

import java.util.Date;

public class ActionError {
    @Getter
    private String url;

    @Getter
    private String message;

    @Getter
    private String exception;
}
