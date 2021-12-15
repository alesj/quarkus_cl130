package com.alesj.qcl.common.rest;

import io.smallrye.config.ConfigMapping;

/**
 * @author Ales Justin
 */
@ConfigMapping(prefix = "qcltest")
public interface TestProperties {
    String version();
}
