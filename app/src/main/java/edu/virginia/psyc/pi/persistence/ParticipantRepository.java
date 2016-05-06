package edu.virginia.psyc.pi.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/19/14
 * Time: 4:42 PM
 * This causes Spring to automatically create CRUD operations for the
 * participant object:
 *    delete(T entity) which deletes the entity given as a parameter.
 *    findAll() which returns a list of entities.
 *    findOne(ID id) which returns the entity using the id given a parameter as a search criteria.
 *    save(T entity) which saves the entity given as a parameter.
 * Additional methods will be provided automatically by following a standard
 * naming convention, as is the case with findByEmailAddress
 */
public interface ParticipantRepository extends JpaRepository<ParticipantDAO, Long>, ParticipantRepositoryCustom {

    ParticipantDAO findByEmail(String email);

    @Query(" select p from ParticipantDAO as p" +
            " where lower(p.fullName) like '%' || lower(:search) || '%'" +
            " or lower(p.email) like '%' || lower(:search) || '%'" +
            " order by lower(p.fullName)")
    Page<ParticipantDAO> search(@Param("search") String search, Pageable pageable);


    @Query("SELECT p FROM ParticipantDAO as p LEFT JOIN p.passwordTokenDAO t \n" +
            "            where t.token = :token")
    ParticipantDAO findByToken(@Param("token") String token);


}
