package com.alesj.qcl.common.rest;

/**
 * @author Ales Justin
 */
public class Input {
    public String value;

    public Input(String value) {
        this.value = value;
    }

    public Input fromString(String string) {
        return new Input(string);
    }
}
