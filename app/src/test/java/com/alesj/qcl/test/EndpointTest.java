package com.alesj.qcl.test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static io.restassured.RestAssured.get;

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
    public void testHelloWorldServiceUsingBlockingStub() {
        String response = get(url).asString();
        Assertions.assertEquals(response, "Hello neo");
    }

}
