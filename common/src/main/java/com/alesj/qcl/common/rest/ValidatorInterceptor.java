package com.alesj.qcl.common.rest;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Ales Justin
 */
@Validateable
@Interceptor
public class ValidatorInterceptor {

    private static Method validate;

    @Inject
    Validator validator;

    @AroundInvoke
    public Object validate(InvocationContext context) throws Throwable {
        Set<ConstraintViolation<Object>> violations = getConstraintViolations(context);
        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
        return context.proceed();
    }

    private static synchronized Method getValidate() throws Throwable {
        if (validate == null) {
            Class<?> clazz = Class.forName("org.hibernate.validator.internal.engine.ValidatorImpl");
            validate = clazz.getDeclaredMethod(
                "validateParameters",
                Object.class, Executable.class, Object[].class, Class[].class);
            validate.setAccessible(true);
        }
        return validate;
    }

    @SuppressWarnings("unchecked")
    private Set<ConstraintViolation<Object>> getConstraintViolations(InvocationContext context) throws Throwable {
        try {
            return (Set<ConstraintViolation<Object>>) getValidate().invoke(
                validator,
                null, context.getMethod(), context.getParameters(), new Class[0]
            );
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}