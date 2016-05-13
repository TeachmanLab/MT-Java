package edu.virginia.psyc.pi.persistence;

import edu.virginia.psyc.pi.domain.PiParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data provides basic Crud operations (find, save, delete ...)
 * on PiParticipant when it finds this interface.
 */
public interface PiParticipantRepository extends JpaRepository<PiParticipant, Long> {
    PiParticipant findByEmail(String email);
}
