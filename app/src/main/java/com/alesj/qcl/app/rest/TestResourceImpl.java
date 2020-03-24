package com.alesj.qcl.app.rest;

import com.alesj.qcl.common.rest.TestResource;
import com.alesj.qcl.engine.TablesRegistry;
import com.alesj.qcl.engine.hibernate.TableModCount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class TestResourceImpl implements TestResource {

    @Inject
    TablesRegistry registry;

    @Override
    public String info() {
        SessionFactory sf = registry.sessionFactory();
        try (Session session = sf.openSession()) {
            Query<TableModCount> query = session.createQuery("SELECT tmc FROM TableModCount tmc", TableModCount.class);
            query.getResultList();
        }
        return "Quarkus CL issue";
    }
}
