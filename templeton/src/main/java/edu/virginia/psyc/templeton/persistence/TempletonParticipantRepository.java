package edu.virginia.psyc.templeton.persistence;

import edu.virginia.psyc.templeton.domain.TempletonParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data provides basic Crud operations (find, save, delete ...)
 * on PiParticipant when it finds this interface.
 */
public interface TempletonParticipantRepository extends JpaRepository<TempletonParticipant, Long> {
    TempletonParticipant findByEmail(String email);
}
