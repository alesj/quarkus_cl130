package com.alesj.qcl.test;

import examples.Greeter;
import examples.GreeterGrpc;
import examples.HelloReply;
import examples.HelloRequest;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.runtime.stork.StorkMeasuringGrpcInterceptor;
import io.quarkus.grpc.runtime.supports.EventLoopBlockingCheckInterceptor;
import io.quarkus.grpc.runtime.supports.IOThreadClientInterceptor;
import io.quarkus.grpc.stubs.UniStreamObserver;
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
    public void testSmoke() {

        int port = 8081;
        ClientInterceptor[] interceptors = new ClientInterceptor[3];
        interceptors[0] = new EventLoopBlockingCheckInterceptor();
        interceptors[1] = new IOThreadClientInterceptor();
        interceptors[2] = new StorkMeasuringGrpcInterceptor();
        // etc
        Channel channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build();
        GreeterGrpc.GreeterStub stub = GreeterGrpc
            .newStub(channel)
            .withInterceptors(interceptors);

        HelloRequest request = HelloRequest.newBuilder().setName("Holly").build();

        HelloReply reply1 = greeter.sayHello(request).await().indefinitely();

        HelloReply[] reply2 = new HelloReply[1];
        StreamObserver<HelloReply> observer = new StreamObserver<>() {
            @Override
            public void onNext(HelloReply value) {
                reply2[0] = value;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };
        stub.sayHello(request, observer);

        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc
            .newBlockingStub(channel)
            .withInterceptors(interceptors);

        HelloReply reply3 = blockingStub.sayHello(request);
    }

}
