package com.alesj.qcl.app;

import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    public void startInput(
        @Observes StartupEvent evt
    ) {
        log.info("Started input...");
    }

}
