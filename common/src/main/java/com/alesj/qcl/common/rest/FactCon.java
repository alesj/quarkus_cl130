package com.alesj.qcl.common.rest;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Ales Justin
 */
@Target({PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = FactCV.class)
public @interface FactCon {
    CatType[] anyOf();
    String message() default "must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
