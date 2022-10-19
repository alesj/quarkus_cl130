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
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.client.GrpcClientChannel;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ales Justin
 */
public abstract class SimpleTestBase {

    abstract int port();

    abstract Vertx vertx();

    abstract void close(Vertx vertx);

    private InputStream stream(String resource) throws IOException {
        return getClass().getClassLoader().getResourceAsStream(resource);
    }

    @Test
    public void testSmoke() throws Exception {
        SslContextBuilder builder = GrpcSslContexts.forClient();
        builder.trustManager(stream("tls/ca.pem"));
        builder.keyManager(stream("tls/client.pem"),
            stream("tls/client.key"));
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
            options.setUseAlpn(true);
            options.setSsl(true);
            Buffer buffer;
            try (InputStream stream = stream("tls/ca.pem")) {
                buffer = Buffer.buffer(stream.readAllBytes());
            }
            Buffer cb;
            try (InputStream stream = stream("tls/client.pem")) {
                cb = Buffer.buffer(stream.readAllBytes());
            }
            Buffer ck;
            try (InputStream stream = stream("tls/client.key")) {
                ck = Buffer.buffer(stream.readAllBytes());
            }
            options.setTrustOptions(new PemTrustOptions().addCertValue(buffer));
            options.setKeyCertOptions(new PemKeyCertOptions().setCertValue(cb).setKeyValue(ck));
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
