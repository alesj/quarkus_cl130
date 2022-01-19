package com.alesj.qcl.test;

import com.alesj.qcl.common.rest.Fact;
import com.alesj.qcl.common.rest.Input;
import com.alesj.qcl.common.rest.TestService;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * @author Ales Justin
 */
@QuarkusTest
public class SimpleTest {

    @Inject
    @RestClient
    TestService service;

    @Test
    public void testSmoke() {
        Fact fact = service.fact(new Input("foobar"));
        System.out.println("fact = " + fact);

        List<Fact> facts = service.facts(Set.of(new Input("foobar"), new Input("baz")));
        System.out.println("facts = " + facts);
    }
}
