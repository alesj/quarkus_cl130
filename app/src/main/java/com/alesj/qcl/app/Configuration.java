package com.alesj.qcl.app;

import examples.GreeterGrpc;
import examples.HelloReply;
import examples.HelloRequest;
import io.grpc.ChannelCredentials;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.InsecureServerCredentials;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerCredentials;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.xds.XdsChannelCredentials;
import io.grpc.xds.XdsServerBuilder;
import io.grpc.xds.XdsServerCredentials;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    @ConfigProperty(name = "xds.port", defaultValue = "30051")
    int port;

    @ConfigProperty(name = "xds.type", defaultValue = "SERVER")
    AppType type;

    @ConfigProperty(name = "xds.target", defaultValue = "localhost:30051")
    String target;

    @ConfigProperty(name = "xds.user", defaultValue = "Ales")
    String user;

    @ConfigProperty(name = "xds.secure", defaultValue = "false")
    boolean secure;

    private Server server;

    private ManagedChannel channel;
    private GreeterGrpc.GreeterBlockingStub stub;
    private ScheduledExecutorService service;

    public void start(@Observes StartupEvent evt) {
        log.info("App type: {}, secure: {}", type, secure);
        switch (type) {
            case SERVER:
                startServer();
                return;
            case CLIENT:
                startClient();
        }
    }

    public void stop(@Observes ShutdownEvent evt) throws Exception {
        switch (type) {
            case SERVER:
                stopServer();
                return;
            case CLIENT:
                stopClient();
        }
    }

    private void startClient() {
        log.info("Starting xDS client -> {}", target);
        ChannelCredentials credentials = InsecureChannelCredentials.create();
        if (secure) {
            credentials = XdsChannelCredentials.create(credentials);
        }
        channel = Grpc.newChannelBuilder(target, credentials).build();
        stub = GreeterGrpc.newBlockingStub(channel);
        service = new ScheduledThreadPoolExecutor(2);
        service.scheduleAtFixedRate(this::greet, 5, 15, TimeUnit.SECONDS);
    }

    private void stopClient() throws Exception {
        if (service != null) {
            service.shutdown();
        }
        if (channel != null) {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private void startServer() {
        new Thread(() -> {
            try {
                ServerCredentials credentials = InsecureServerCredentials.create();
                if (secure) {
                    credentials = XdsServerCredentials.create(credentials);
                }
                server = XdsServerBuilder.forPort(port, credentials)
                    .addService(new HelloService())
                    .addService(ProtoReflectionService.newInstance()) // convenient for command line tools
                    .build()
                    .start();
                log.info("Listening on port " + port);
                server.awaitTermination();
            } catch (Exception e) {
                log.error("Error: ", e);
                Quarkus.asyncExit();
            }
        }, "xDS Server").start();
    }

    private void stopServer() {
        if (server == null) return;

        // Start graceful shutdown
        server.shutdown();
        try {
            // Wait for RPCs to complete processing
            if (!server.awaitTermination(30, TimeUnit.SECONDS)) {
                // That was plenty of time. Let's cancel the remaining RPCs
                server.shutdownNow();
                // shutdownNow isn't instantaneous, so give a bit of time to clean resources up
                // gracefully. Normally this will be well under a second.
                server.awaitTermination(5, TimeUnit.SECONDS);
            }
        } catch (InterruptedException ex) {
            server.shutdownNow();
        }
    }

    /** Say hello to server. */
    private void greet() {
        log.info("Will try to greet " + user + " ...");
        String msg = user + "::" + ThreadLocalRandom.current().nextInt(100);
        HelloRequest request = HelloRequest.newBuilder().setName(msg).build();
        HelloReply response;
        try {
            response = stub.sayHello(request);
        } catch (StatusRuntimeException e) {
            log.warn("RPC failed: {}", e.getStatus());
            return;
        }
        log.info("Greeting: " + response.getMessage());
    }
}

