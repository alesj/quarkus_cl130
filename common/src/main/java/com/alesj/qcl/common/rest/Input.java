package com.alesj.qcl.common.rest;

/**
 * @author Ales Justin
 */
public class Input implements AutoCloseable {
    private boolean closed;

    public boolean closed() {
        return closed;
    }

    @Override
    public void close() {
        closed = true;
    }
}
