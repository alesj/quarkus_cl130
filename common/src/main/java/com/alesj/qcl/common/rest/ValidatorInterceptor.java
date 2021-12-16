package com.alesj.qcl.common.rest;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.util.Set;

/**
 * @author Ales Justin
 */
@Validateable
@Interceptor
public class ValidatorInterceptor {

    @Inject
    Validator validator;

    @AroundInvoke
    public Object validate(InvocationContext context) throws Throwable {
        ExecutableValidator ev = validator.forExecutables();
        Set<ConstraintViolation<Object>> violations = ev.validateParameters(
            context.getTarget(), context.getMethod(), context.getParameters()
        );
        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
        return context.proceed();
    }
}