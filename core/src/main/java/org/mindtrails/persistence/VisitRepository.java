package org.mindtrails.persistence;

import org.mindtrails.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findAllByNameEndsWith(String ending);
}
