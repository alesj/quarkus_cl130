package com.alesj.qcl.app;

import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerCredentials;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.xds.XdsServerBuilder;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.concurrent.TimeUnit;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    @ConfigProperty(name = "xds.port", defaultValue = "30051")
    int port;

    private Server server;

    public void startServer(@Observes StartupEvent evt) throws Exception {
        new Thread(() -> {
            try {
                ServerCredentials credentials = InsecureServerCredentials.create();
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

    public void stopServer(@Observes ShutdownEvent evt) {
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
}

