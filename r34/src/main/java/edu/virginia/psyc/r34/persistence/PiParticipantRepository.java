package edu.virginia.psyc.r34.persistence;

import edu.virginia.psyc.r34.domain.PiParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data provides basic Crud operations (find, save, delete ...)
 * on PiParticipant when it finds this interface.
 */
public interface PiParticipantRepository extends JpaRepository<PiParticipant, Long> {
    PiParticipant findByEmail(String email);
}
