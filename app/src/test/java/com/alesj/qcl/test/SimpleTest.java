package com.alesj.qcl.test;

import examples.Greeter;
import examples.HelloReply;
import examples.HelloRequest;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/**
 * @author Ales Justin
 */
@QuarkusTest
public class SimpleTest {

    @GrpcClient
    Greeter greeter;

    @Test
    public void testSmoke() throws Exception {
        HelloRequest request = HelloRequest.newBuilder().setName("Ales").build();
        HelloReply reply1 = greeter.sayHello(request).await().indefinitely();
        System.out.println("reply1 = " + reply1);

        Thread.sleep(10L); // so that the scheduler kicks in
    }

}
