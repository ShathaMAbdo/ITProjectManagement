package se.BTH.ITProjectManagement.sprintManag.validators;

import se.BTH.ITProjectManagement.sprintManag.Annotations.PositiveNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PositiveNumberValidator implements ConstraintValidator<PositiveNumber, Integer> {

    @Override
    public void initialize(PositiveNumber contactNumber) {
    }

    @Override
    public boolean isValid(Integer contactField, ConstraintValidatorContext cxt) {
        return ((contactField != null )&& (contactField > -1)) ;
    }

}