package com.alesj.qcl.app;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    @ConfigProperty(name = "xds.user", defaultValue = "Ales")
    String user;

    /**
     * Say hello to server.
     */
    @Scheduled(every = "10s", delayUnit = TimeUnit.SECONDS)
    void greet() {
        log.info("Will try to greet " + user + " ...");
    }
}

