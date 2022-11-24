package com.alesj.qcl.app;

import examples.Greeter;
import examples.HelloReply;
import examples.HelloRequest;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

import javax.inject.Singleton;

/**
 * @author Ales Justin
 */
@GrpcService
@Singleton
public class HelloService implements Greeter {
    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        return Uni.createFrom().item(reply);
    }
}
