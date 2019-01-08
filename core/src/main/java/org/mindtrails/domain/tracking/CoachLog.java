package org.mindtrails.domain.tracking;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.data.DoNotDelete;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="CoachLog")
@DoNotDelete
@Data
public class CoachLog implements Comparable<CoachLog> {

    @Id
    @GeneratedValue
    protected long id;

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("participant")
    protected Participant participant;

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("coach")
    protected Participant coach;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    Date dateAttempted;
    String coachingSession;
    boolean successful;

    @Override
    public int compareTo(CoachLog o) {
        if(this.dateAttempted == null) return 0;
        if(o.dateAttempted == null) return 1;
        return this.dateAttempted.compareTo(o.dateAttempted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoachLog coachLog = (CoachLog) o;
        return id == coachLog.id &&
                participant.getId() == coachLog.participant.getId() &&
                Objects.equals(dateAttempted, coachLog.dateAttempted) &&
                Objects.equals(coachingSession, coachLog.coachingSession) &&
                Objects.equals(successful, coachLog.successful);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, dateAttempted, coachingSession, successful);
    }
}

