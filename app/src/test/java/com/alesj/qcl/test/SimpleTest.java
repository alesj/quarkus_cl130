package com.alesj.qcl.test;

import com.example.grpc.exc.HelloReply;
import com.example.grpc.exc.HelloRequest;
import com.example.grpc.exc.LegacyHelloGrpcGrpc;
import io.grpc.Channel;
import io.grpc.netty.NettyChannelBuilder;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/**
 * @author Ales Justin
 */
@QuarkusTest
public class SimpleTest {

    @GrpcClient
    LegacyHelloGrpcGrpc.LegacyHelloGrpcBlockingStub stub;

    @Test
    void legacySayHello() {
        System.out.println("stub = " + stub.legacySayHello(HelloRequest.newBuilder().setName("Neo").build()));
    }

    @Test
    public void threadsCheck() {
        for (int i = 0; i < 10; i++) {
            Channel channel = NettyChannelBuilder.forTarget("dns:///localhost:9001").usePlaintext().build();
            System.out.println(i + " - channel = " + channel);
            LegacyHelloGrpcGrpc.LegacyHelloGrpcBlockingStub stub = LegacyHelloGrpcGrpc.newBlockingStub(channel);
            HelloReply reply = stub.legacySayHello(HelloRequest.newBuilder().setName("Neo" + i).build());
            System.out.println("reply = " + reply.getMessage());
        }
    }
}
