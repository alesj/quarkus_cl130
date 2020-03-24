package com.alesj.qcl.engine.hibernate;

import com.alesj.qcl.engine.TablesRegistry;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.internal.TcclLookupPrecedence;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.hibernate.tool.schema.Action;

import java.util.Objects;
import java.util.function.Function;
import javax.sql.DataSource;

/**
 * @author Ales Justin
 */
public class HibernateTablesRegistry implements TablesRegistry {
    private final DataSource dataSource;
    private final JtaPlatform jtaPlatform;
    private final Class<? extends Dialect> dialect;
    private final Function<?, ?> registerFn;

    private SessionFactory sessionFactory;

    public <D extends Dialect> HibernateTablesRegistry(
        DataSource dataSource,
        JtaPlatform jtaPlatform,
        Class<D> dialect,
        Function<?, ?> registerFn
    ) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource");
        this.jtaPlatform = Objects.requireNonNull(jtaPlatform, "jtaPlatform");
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.registerFn = Objects.requireNonNull(registerFn, "registerFn");
    }

    @Override
    public SessionFactory sessionFactory() {
        if (sessionFactory == null) {
            start();
        }
        return sessionFactory;
    }

    protected synchronized void start() {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySetting(AvailableSettings.DATASOURCE, dataSource)
            .applySetting(AvailableSettings.TRANSACTION_COORDINATOR_STRATEGY, "jta")
            .applySetting(AvailableSettings.JTA_PLATFORM, jtaPlatform)
            .applySetting(AvailableSettings.HBM2DDL_AUTO, Action.CREATE_ONLY)
            .applySetting(AvailableSettings.DIALECT, dialect.getName())
            .applySetting(AvailableSettings.TC_CLASSLOADER, TcclLookupPrecedence.NEVER)
            .addService(ClassLoaderService.class, new ClassLoaderServiceImpl(registerFn.getClass().getClassLoader()))
            .build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(TableModCount.class);

        Metadata metadata = metadataSources
            .getMetadataBuilder()
            .applyImplicitNamingStrategy(ImplicitNamingStrategyComponentPathImpl.INSTANCE)
            .build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    public synchronized void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}
