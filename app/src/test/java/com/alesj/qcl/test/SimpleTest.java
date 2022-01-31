package com.alesj.qcl.test;

import com.alesj.qcl.common.rest.Input;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Ales Justin
 */
@QuarkusTest
@QuarkusTestResource(SimpleTestResource.class)
public class SimpleTest {

    @Inject
    @Named("myInput")
    Input input;

    @Test
    public void testSmoke() {
        SimpleTestResource.input = input;
    }
}
