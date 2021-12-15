package com.alesj.qcl.common.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author Ales Justin
 */
@RegisterRestClient(configKey = "qcltest")
public interface TestService {
    @GET
    @Path("/fact")
    Fact fact();
}
