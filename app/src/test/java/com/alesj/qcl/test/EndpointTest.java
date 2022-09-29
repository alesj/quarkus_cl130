package com.alesj.qcl.test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.impl.KeyStoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestProfile(NewVertxGrpcTestProfile.class)
class EndpointTest {

    @TestHTTPResource(value = "/hello/blocking/neo", ssl = true)
    URL url;

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.useRelaxedHTTPSValidation();
    }

    @AfterAll
    public static void restoreRestAssured() {
        RestAssured.reset();
    }

    @Test
    public void testHelloWorldServiceUsingBlockingStub() throws Exception {
        KeyStore ts = KeyStoreHelper.loadKeyCert(
            List.of(buffer("/Users/alesj/projects/redhat/quarkus/integration-tests/grpc-mutual-auth/src/main/resources/tls/client.key")),
            List.of(buffer("/Users/alesj/projects/redhat/quarkus/integration-tests/grpc-mutual-auth/src/main/resources/tls/client.pem"))
        );

        RequestSpecification spec = new RequestSpecBuilder()
            .setTrustStore(ts)
            .setKeyStore("/Users/alesj/projects/redhat/quarkus/integration-tests/grpc-mutual-auth/src/main/resources/tls/ca.pem", "123456")
            .build();

        String response = given().spec(spec).get(url).asString();
        Assertions.assertEquals(response, "Hello neo");
    }

    private Buffer buffer(String resource) {
        try {
            return Buffer.buffer(Files.readAllBytes(Path.of(resource)));
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

}
