package com.alesj.qcl.app;

import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class QCLAppConfiguration {
    @Produces
    @Singleton
    public Logger logger() {
        return LoggerFactory.getLogger(QCLAppConfiguration.class);
    }

    public void start(@Observes StartupEvent event, Logger logger) {
        logger.info("Started ...");
    }
}
