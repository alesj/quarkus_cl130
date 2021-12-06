package com.alesj.qcl.app.rest;

import com.alesj.qcl.common.rest.TestResource;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Ales Justin
 */
@ApplicationScoped
public class TestResourceImpl implements TestResource {
    @Override
    public String info() {
        return "Quarkus ftw!";
    }
}
