package com.alesj.qcl.test;

import examples.GreeterGrpc;
import examples.HelloReply;
import examples.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Vertx;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.client.GrpcClientChannel;
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

    @Test
    public void testNewSmoke() {
        Vertx vertx = Vertx.vertx();
        try {
            GrpcClient client = GrpcClient.client(vertx);
            try {
                GrpcClientChannel channel = new GrpcClientChannel(client, SocketAddress.inetSocketAddress(8081, "localhost"));
                GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
                HelloReply reply = stub.sayHello(HelloRequest.newBuilder().setName("neo-blocking").build());
                System.out.println("reply = " + reply);
            } finally {
                client.close().onComplete(System.out::println);
            }
        } finally {
            vertx.close();
        }
    }
}
