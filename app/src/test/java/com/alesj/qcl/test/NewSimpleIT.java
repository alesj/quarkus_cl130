package com.alesj.qcl.test;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;
import io.vertx.core.Vertx;

/**
 * @author Ales Justin
 */
@QuarkusIntegrationTest
@TestProfile(NewVertxGrpcTestProfile.class)
public class NewSimpleIT extends SimpleTestBase {
    @Override
    int port() {
        return 8081;
    }

    @Override
    Vertx vertx() {
        return Vertx.vertx();
    }

    @Override
    void close(Vertx vertx) {
        vertx.close().toCompletionStage().toCompletableFuture().join();
    }
}
