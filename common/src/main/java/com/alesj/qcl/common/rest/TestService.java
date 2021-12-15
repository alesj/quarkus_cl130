package com.alesj.qcl.common.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author Ales Justin
 */
@RegisterRestClient(configKey = "qcltest")
public interface TestService {
    @Validateable
    @GET
    @Path("/{type}")
    Fact fact(
        @Valid
        @FactCon(anyOf = {CatType.fact})
        @PathParam("type")
            CatType ct
    );
}
