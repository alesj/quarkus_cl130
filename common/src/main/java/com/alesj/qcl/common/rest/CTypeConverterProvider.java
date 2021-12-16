package com.alesj.qcl.common.rest;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Ales Justin
 */
public class CTypeConverterProvider implements ParamConverterProvider {
    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (CType.class.equals(rawType)) {
            return (ParamConverter<T>) new ParamConverter<CType>() {
                @Override
                public CType fromString(String value) {
                    return null;
                }

                @Override
                public String toString(CType value) {
                    return value.getType();
                }
            };
        }
        return null;
    }
}
