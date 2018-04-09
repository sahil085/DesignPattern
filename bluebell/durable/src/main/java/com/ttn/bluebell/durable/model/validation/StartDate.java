package com.ttn.bluebell.durable.model.validation;

import com.ttn.bluebell.durable.model.validation.validators.StartDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by ttnd on 30/9/16.
 */
@Constraint(validatedBy = {StartDateValidator.class})
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,ElementType.LOCAL_VARIABLE})
@Retention( value = RetentionPolicy.RUNTIME)
@Documented
public @interface StartDate {

    String message() default "{start.date.lessThan.currentDate}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

