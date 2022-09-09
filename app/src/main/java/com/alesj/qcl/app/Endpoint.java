package com.alesj.qcl.app;

import io.quarkus.grpc.GrpcClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import examples.GreeterGrpc;
import examples.HelloRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ales Justin
 */
@Path("/hello")
public class Endpoint {

    Logger log = LoggerFactory.getLogger(Endpoint.class);

    @GrpcClient("hello")
    GreeterGrpc.GreeterBlockingStub blockingHelloService;

    @GET
    @Path("/blocking/{name}")
    public String helloBlocking(@PathParam("name") String name) {
        log.info("Name: {}", name);
        // if (true) throw new IllegalArgumentException("Name: " + name);
        return blockingHelloService.sayHello(HelloRequest.newBuilder().setName(name).build()).getMessage();
    }

}
