package com.alesj.qcl.test;

import examples.GreeterGrpc;
import examples.HelloReply;
import examples.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/**
 * @author Ales Justin
 */
@QuarkusTest
public class SimpleTest {

    @Test
    public void testSmoke() {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 8081).build();
        try {
            GreeterGrpc.GreeterBlockingStub client = GreeterGrpc.newBlockingStub(channel);
            HelloReply reply = client
                .sayHello(HelloRequest.newBuilder().setName("neo-blocking").build());
            System.out.println("reply = " + reply);
        } finally {
            channel.shutdown();
        }
    }
}
