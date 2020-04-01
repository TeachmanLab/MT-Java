package org.mindtrails.persistence;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.ParticipantStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;

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
 * Additional methods wil be provided automatically by following a standard
 * naming convention, as is the case with findByEmailAddress
 */

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Participant findByEmail(String email);
    List<Participant> findByPhone(String phone);
    List<Participant> findByActiveAndPhoneReminders(boolean active, boolean phone);
    List<Participant> findByActive(boolean active);

    @Query(" select p from Participant as p" +
            " where lower(p.fullName) like '%' || lower(:search) || '%'" +
            " or lower(p.email) like '%' || lower(:search) || '%'" +
            " order by lower(p.fullName)")
    Page<Participant> search(@Param("search") String search, Pageable pageable);


    @Query("SELECT p FROM Participant as p LEFT JOIN p.passwordToken t \n" +
            "            where t.token = :token")
    Participant findByToken(@Param("token") String token);

    Long countByLastLoginDateAfter(Date date);

    long countByLastLoginDateBetween(Date date1,Date date2);

    List<ParticipantStats> findAllStatsBy();

    List<Participant> findParticipantsByTestAccountIsFalseAndAdminIsFalse();


    Page<Participant> findByCoachedBy(Participant coach, Pageable pageable);

    @Query(" select p from Participant as p" +
            " where p.coaching is true" +
            " order by lower(p.fullName)")
    List<Participant> findCoaches();

    @Query("SELECT p FROM Participant as p LEFT JOIN p.study s \n" +
            "where s.conditioning = :condition and " +
            "p.coachedBy is null and p.testAccount = false order by " +
            "p.lastLoginDate desc")
    Page<Participant> findEligibleForCoaching(@Param("condition") String condition,
                                              Pageable pageable);


    @Query("SELECT p FROM Participant as p LEFT JOIN p.study s \n" +
            " where lower(p.fullName) like '%' || lower(:search) || '%'" +
            " or lower(p.email) like '%' || lower(:search) || '%' and " +
            "s.conditioning = :condition and " +
            "p.coachedBy is null and p.testAccount = false order by " +
            "p.lastLoginDate desc")
    Page<Participant> searchEligibleForCoaching(@Param("condition") String condition,
                                                Pageable pageable,
                                                @Param("search") String search);


    // Find all participants by condition
    @Query("SELECT p FROM Participant as p LEFT JOIN p.study s \n" +
            "            where s.conditioning = :condition")
    List<Participant> findAllByCondition(@Param("condition") String condition);

}
