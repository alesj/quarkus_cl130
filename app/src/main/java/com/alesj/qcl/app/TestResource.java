package com.alesj.qcl.app;

import com.alesj.qcl.common.rest.Fact;
import com.alesj.qcl.common.rest.Input;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ales Justin
 */
@ApplicationScoped
@Path("")
public class TestResource {
    @POST
    @Path("/form")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Fact formFact(
        @FormParam("input") Input input
    ) {
        Fact fact = new Fact();
        fact.fact = "Input: " + input.value;
        return fact;
    }

    @POST
    @Path("/forms")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public List<Fact> formFacts(
        @FormParam("inputs") Set<Input> inputs
    ) {
        return inputs.stream().map(input -> {
            Fact fact = new Fact();
            fact.fact = "Input: " + input.value;
            return fact;
        }).collect(Collectors.toList());
    }
}
