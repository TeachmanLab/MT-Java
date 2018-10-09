package org.mindtrails.domain.questionnaire;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class MeasureGroup {

    protected final String name;
    protected final String desc;
    protected final Map<Integer, String> scale;  // list of potential values.
    protected List<Measure> measures = new ArrayList<>();

}
