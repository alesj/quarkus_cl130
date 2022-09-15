package com.alesj.qcl.test;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

/**
 * @author Ales Justin
 */
public class NewVertxGrpcTestProfile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
            "quarkus.grpc.clients.hello.port", "8081",
            "quarkus.grpc.server.use-separate-server", "false"
        );
    }
}
