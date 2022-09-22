package com.alesj.qcl.test;

import examples.GreeterGrpc;
import examples.HelloReply;
import examples.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.client.GrpcClientChannel;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * @author Ales Justin
 */
public abstract class SimpleTestBase {

    abstract int port();

    abstract Vertx vertx();

    abstract void close(Vertx vertx);

    @Test
    public void testSmoke() throws Exception {
        SslContextBuilder builder = GrpcSslContexts.forClient();
        builder.trustManager(getClass().getClassLoader().getResourceAsStream("tls/ca.pem"));
        SslContext context = builder.build();

        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", port())
            //.usePlaintext()
            .sslContext(context)
            .build();
        try {
            GreeterGrpc.GreeterBlockingStub client = GreeterGrpc.newBlockingStub(channel);
            HelloReply reply = client
                .sayHello(HelloRequest.newBuilder().setName("neo-blocking").build());
            System.out.println("reply = " + reply);
        } finally {
            channel.shutdownNow();
        }
    }

    @Test
    public void testNewSmoke() throws Exception {
        Vertx vertx = vertx();
        try {
            HttpClientOptions options = new HttpClientOptions();
            Buffer buffer;
            try (InputStream stream = getClass().getClassLoader().getResourceAsStream("tls/ca.pem")) {
                buffer = Buffer.buffer(stream.readAllBytes());
            }
            options.setTrustOptions(new PemTrustOptions().addCertValue(buffer));
            GrpcClient client = GrpcClient.client(vertx, options);
            try {
                GrpcClientChannel channel = new GrpcClientChannel(client, SocketAddress.inetSocketAddress(port(), "localhost"));
                GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
                HelloReply reply = stub.sayHello(HelloRequest.newBuilder().setName("neo-blocking").build());
                System.out.println("reply = " + reply);
            } finally {
                client.close().onComplete(System.out::println).toCompletionStage().toCompletableFuture().join();
            }
        } finally {
            close(vertx);
        }
    }
}
