package org.mindtrails.persistence;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.enhanced.TableGenerator;

import java.io.Serializable;

/**
 * Generate new ids only if an id is not specified.  Otherwise defaults
 * to the standard TableGenerator behavior.  This allows us to pull ids from
 * one instance of Mindtrails to another, and retain the original Id.  There is
 * some danger here, we should test carefully to make sure we don't ever allow
 * an id to get set to a non 0 value.  We'll also likely get a duplicate ID error
 * if we ever try to create new users on a slave instance of Mindtrails.
 */
public class MindtrailsIdGenerator extends TableGenerator {

    @Override
    public synchronized Serializable generate(SessionImplementor session, Object object) {
        Serializable id = session.getEntityPersister(null, object)
                .getClassMetadata().getIdentifier(object, session);
        return (id != null && !id.toString().equals("0")) ? id : super.generate(session, object);
    }
}