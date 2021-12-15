package com.alesj.qcl.app;

import com.alesj.qcl.common.rest.CatType;
import com.alesj.qcl.common.rest.TestService;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class QCLAppConfiguration {
    @Inject
    @RestClient
    TestService service;

    public void start(@Observes StartupEvent event) {
        try {
            System.out.println("event = " + service.fact(CatType.sphynx));
        } catch (ConstraintViolationException ignored) {
        }
    }
}
