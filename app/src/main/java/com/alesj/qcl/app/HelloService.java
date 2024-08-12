package com.alesj.qcl.app;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * @author Ales Justin
 */
@ApplicationScoped
@Path("/app")
public class HelloService {

    @GET
    @Path("/dto")
    public Dto dto() {
        Dto dto = new Dto();
        dto.setResponse("Test1");
        return dto;
    }
}
