package com.alesj.qcl.test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

/**
 * @author Ales Justin
 */
@QuarkusTest
@TestProfile(NewVertxGrpcTestProfile.class)
public class NewSimpleTest extends ManagedSimpleTestBase {
    @Override
    int port() {
        return 8444;
    }
}
