package com.alesj.qcl.test;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

/**
 * @author Ales Justin
 */
@QuarkusIntegrationTest
public class SimpleTestIT {

    @Test
    public void testSmoke() {
        Response response = RestAssured.get("/app/dto");
        System.out.println("response = " + response.asPrettyString());
    }

}
