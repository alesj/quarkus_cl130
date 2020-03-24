package com.alesj.qcl.engine;

import org.hibernate.SessionFactory;

/**
 * @author Ales Justin
 */
public interface TablesRegistry extends AutoCloseable {
    SessionFactory sessionFactory();
}
