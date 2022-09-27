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
            "quarkus.grpc.clients.hello.port", "8444",
            "quarkus.grpc.server.use-separate-server", "false",
            "quarkus.grpc.clients.hello.use-quarkus-grpc-client", "true",
            "quarkus.http.ssl.certificate.file", "tls/server.pem",
            "quarkus.http.ssl.certificate.key-file", "tls/server.key"
        );
    }
}
