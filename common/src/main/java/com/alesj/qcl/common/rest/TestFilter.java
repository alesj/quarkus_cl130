package com.alesj.qcl.common.rest;

import io.quarkus.arc.Unremovable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Ales Justin
 */
@ApplicationScoped
@Provider
//@Unremovable
public class TestFilter implements ClientRequestFilter {

    @Inject
    TestProperties props;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        System.out.println("props.version: " + props.version());
    }
}
