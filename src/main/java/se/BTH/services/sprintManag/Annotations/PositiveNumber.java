package se.BTH.services.sprintManag.Annotations;

import se.BTH.services.sprintManag.validators.PositiveNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PositiveNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveNumber {
    String message() default "The OEstimate number must be positive";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}