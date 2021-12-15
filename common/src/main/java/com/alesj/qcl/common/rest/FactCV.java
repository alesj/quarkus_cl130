package com.alesj.qcl.common.rest;

import javax.enterprise.context.Dependent;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ales Justin
 */
@Dependent
public class FactCV implements ConstraintValidator<FactCon, CatType> {
    private List<CatType> subset;

    @Override
    public boolean isValid(CatType value, ConstraintValidatorContext context) {
        return value != null && subset.contains(value);
    }

    @Override
    public void initialize(FactCon factCon) {
        subset = Arrays.asList(factCon.anyOf());
    }
}
