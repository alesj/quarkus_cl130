package com.alesj.qcl.test;

import io.vertx.core.Vertx;

import javax.inject.Inject;

/**
 * @author Ales Justin
 */
public abstract class ManagedSimpleTestBase extends SimpleTestBase {

    @Inject
    Vertx vertx;

    @Override
    Vertx vertx() {
        return vertx;
    }

    @Override
    void close(Vertx vertx) {
        // do nothing, CDI managed
    }
}
