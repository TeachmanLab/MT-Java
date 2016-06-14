package edu.virginia.psyc.mindtrails.persistence;

import edu.virginia.psyc.mindtrails.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/19/14
 * Time: 4:42 PM
 * This causes Spring to automatically create CRUD opperations for the
 * trial object:
 *    delete(T entity) which deletes the entity given as a parameter.
 *    findAll() which returns a list of entities.
 *    findOne(ID id) which returns the entity using the id given a parameter as a search criteria.
 *    save(T entity) which saves the entity given as a parameter.
 */
public interface VisitRepository extends JpaRepository<Visit, Long> {}
