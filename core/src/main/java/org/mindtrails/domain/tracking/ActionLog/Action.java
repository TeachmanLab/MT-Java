package org.mindtrails.domain.tracking.ActionLog;

import lombok.Getter;

import java.util.Date;

public class Action {
    @Getter
    private String name;

    @Getter
    private String value;

    @Getter
    private int latency;
}
