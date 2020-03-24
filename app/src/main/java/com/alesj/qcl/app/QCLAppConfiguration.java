package com.alesj.qcl.app;

import com.alesj.qcl.engine.TablesRegistry;
import com.alesj.qcl.engine.hibernate.CustomH2Dialect;
import com.alesj.qcl.engine.hibernate.HibernateTablesRegistry;
import io.quarkus.hibernate.orm.runtime.customized.QuarkusJtaPlatform;
import io.quarkus.runtime.ShutdownEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class QCLAppConfiguration {

    @Produces
    @ApplicationScoped
    public TablesRegistry tablesRegistry(Instance<DataSource> dataSource) {
        return new HibernateTablesRegistry(
            dataSource.get(),
            QuarkusJtaPlatform.INSTANCE,
            CustomH2Dialect.class,
            k -> 42
        );
    }

    public void destroy(@Observes ShutdownEvent event, TablesRegistry registry) throws Exception {
        registry.close();
    }

}
