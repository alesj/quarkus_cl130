package com.alesj.qcl.test;

import io.vertx.core.Vertx;

import java.util.concurrent.TimeUnit;

/**
 * @author Ales Justin
 */
public class TestMain {
    public static void main(String[] args) throws Exception {
        NewSimpleTest test = new NewSimpleTest();
        test.vertx = Vertx.vertx();
        //test.testSmoke();
        test.testNewSmoke();
        test.vertx.close().toCompletionStage().toCompletableFuture().get(10, TimeUnit.SECONDS);
    }
}
