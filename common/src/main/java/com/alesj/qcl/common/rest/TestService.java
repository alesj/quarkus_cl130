package com.alesj.qcl.common.rest;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Set;

/**
 * @author Ales Justin
 */
@RegisterRestClient(configKey = "qcltest")
@RegisterProvider(InputParamConverterProvider.class)
public interface TestService {
    @POST
    @Path("/form")
    @Consumes("application/x-www-form-urlencoded")
    Fact fact(
        @FormParam("input") Input input
    );

    @POST
    @Path("/forms")
    @Consumes("application/x-www-form-urlencoded")
    List<Fact> facts(
        @FormParam("inputs") Set<Input> inputs
    );
}
