package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 5/26/14
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface QuestionnaireData {

    public int getId();

    public void setId(int id);

    public ParticipantDAO getParticipantDAO();

    public void setParticipantDAO(ParticipantDAO participantDAO);

    public Date getDate();

    public void setDate(Date date);

    public Session.NAME getSession();

    public void setSession(Session.NAME session);

}
