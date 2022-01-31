package com.alesj.qcl.test;

import com.alesj.qcl.common.rest.Input;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

/**
 * @author Ales Justin
 */
public class SimpleTestResource implements QuarkusTestResourceLifecycleManager {
    static Input input;

    @Override
    public Map<String, String> start() {
        return Map.of();
    }

    @Override
    public void stop() {
        if (!input.closed()) {
            throw new IllegalStateException();
        }
    }
}
