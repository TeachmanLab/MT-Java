package org.mindtrails.service;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Study;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

/**
 * Provides a means to create and save participants in custom ways.
 */
public interface ParticipantService {

    /**
     * Creates a new object that is an instance of, or extension of
     * Participant.  Be certain it set's the participants Study!
     */
    Participant create();

    /**
     * Returns a list of Possible Studies this service would assign a Participant.
     */
    List<Study> getStudies();

    /** Returns a participant associated with given spring security
     * model.
     */
    Participant get(Principal p);

    /** Returns a participant associated with given email
     */
    Participant findByEmail(String email);

    /**
     * Checks for eligibility - usually with a form
     * stored against the current users session
     */
    boolean isEligible(HttpSession session);

    /**
     * When saving an object for the first time, there may
     * be data lingering in the session that needs to be
     * collected and associated with the Participant.  This
     * allows that data to be captured and associated.
     * @param p
     * @param session The current HTTP Session.
     */
    void saveNew(Participant p, HttpSession session);

    void save(Participant p);

    void flush();

    /** Returns the total number of participants **/
    long count();

}
