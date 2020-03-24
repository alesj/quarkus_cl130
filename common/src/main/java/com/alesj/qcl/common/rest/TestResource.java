package com.alesj.qcl.common.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * A JAX-RS interface.  An implementation of this interface must be provided.
 *
 * @author Ales Justin
 */
@Path("/api")
public interface TestResource {
    @GET
    String info();
}
