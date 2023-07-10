package com.alesj.qcl.app;

import examples.GreeterGrpc;
import examples.HelloReply;
import examples.HelloRequest;
import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    @ConfigProperty(name = "xds.user", defaultValue = "Ales")
    String user;

    @GrpcClient
    GreeterGrpc.GreeterBlockingStub stub;

    /**
     * Say hello to server.
     */
    @Scheduled(every = "10s", delay = 10L, delayUnit = TimeUnit.SECONDS)
    void greet() {
        log.info("Will try to greet " + user + " ...");
        String msg = user + "::" + ThreadLocalRandom.current().nextInt(100);
        HelloRequest request = HelloRequest.newBuilder().setName(msg).build();
        HelloReply response;
        try {
            response = stub.sayHello(request);
        } catch (StatusRuntimeException e) {
            log.warn("RPC failed:", e);
            return;
        }
        log.info("Greeting: " + response.getMessage());
    }
}

