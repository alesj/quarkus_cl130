package com.alesj.qcl.test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * @author Ales Justin
 */
@QuarkusTest
public class SimpleTest extends ManagedSimpleTestBase {
    @Override
    int port() {
        return 9001;
    }
}
