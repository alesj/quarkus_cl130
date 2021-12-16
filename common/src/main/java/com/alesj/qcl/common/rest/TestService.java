package com.alesj.qcl.common.rest;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author Ales Justin
 */
@RegisterRestClient(configKey = "qcltest")
@RegisterProvider(RequestFilter.class)
@RegisterProvider(CTypeConverterProvider.class)
public interface TestService {
    @GET
    @Path("/{type}")
    Fact fact(
        @PathParam("type") CType type
    );
}
