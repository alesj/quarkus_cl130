package com.alesj.qcl.app;

import com.alesj.qcl.common.rest.Input;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Disposes;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    @Singleton
    @Named("myInput")
    public Input input() {
        return new Input();
    }

    public void startInput(
        @Observes StartupEvent evt,
        @Named("myInput") Input input
    ) {
        log.info("Started input...");
    }

    public void stopInput(
        @Disposes @Named("myInput") AutoCloseable input
    ) throws Exception {
        log.info("Stopping input...");
        input.close();
    }

}
