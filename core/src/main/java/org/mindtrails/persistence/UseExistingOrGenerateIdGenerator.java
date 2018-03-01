//package org.mindtrails.persistence;
//
//import org.hibernate.HibernateException;
//import org.hibernate.MappingException;
//import org.hibernate.dialect.Dialect;
//import org.hibernate.engine.spi.SessionImplementor;
//import org.hibernate.id.Configurable;
//import org.hibernate.id.IdentifierGenerator;
//import org.hibernate.id.enhanced.TableGenerator;
//import org.hibernate.service.ServiceRegistry;
//import org.hibernate.type.Type;
//
//import java.io.Serializable;
//import java.util.Properties;
//
//
//public class UseExistingOrGenerateIdGenerator implements IdentifierGenerator, Configurable {
//
//    private IdentifierGenerator defaultGenerator;
//
//    @Override
//    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
//    // For example: take a class name and create an instance
//        this.defaultGenerator = buildGeneratorFromParams(
//            params.getProperty("default"));
//    }
//
//    @Override
//    public Serializable generate(SessionImplementor session, Object object)
//            throws HibernateException {
//        Serializable id = session.getEntityPersister(null, object)
//                .getClassMetadata().getIdentifier(object, session);
//        return id != null ? id : defaultGenerator.generate(session, object);
//    }
//}
