package edu.virginia.psyc.pi.persistence;

import edu.virginia.psyc.pi.domain.Participant;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/24/14
 * Time: 1:27 PM
 * The ParticipantRepository class is automatically implemented
 * with basic crud operations, but we want to add in an entity2domain
 * opperation as well, and we do that this way ...
 */
public interface ParticipantRepositoryCustom {

    public Participant entityToDomain(ParticipantDAO dao);

    public void domainToEntity(Participant p , ParticipantDAO dao);

}
