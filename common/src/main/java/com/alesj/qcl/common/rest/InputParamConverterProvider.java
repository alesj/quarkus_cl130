package com.alesj.qcl.common.rest;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Ales Justin
 */
public class InputParamConverterProvider implements ParamConverterProvider {
    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType == Input.class) {
            return (ParamConverter<T>) new ParamConverter<Input>() {
                @Override
                public Input fromString(String value) {
                    return new Input(value);
                }

                @Override
                public String toString(Input value) {
                    return value.value;
                }
            };
        }
        return null;
    }
}
