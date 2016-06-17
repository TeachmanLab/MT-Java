package edu.virginia.psyc.mindtrails.service;

import edu.virginia.psyc.mindtrails.domain.Participant;

import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * Provides a means to create and save participants in custom ways.
 */
public interface ParticipantService {

    /**
     * Creates a new object that is an instance of, or extension of
     * Participant.
     */
    Participant create();

    /** Returns a participant associated with given spring security
     * model.
     */
    Participant get(Principal p);

    Participant findByEmail(String email);

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
