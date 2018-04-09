package com.ttn.bluebell.durable.model.validation;

import com.ttn.bluebell.durable.model.validation.validators.ProjectCreateValidator;

import javax.validation.Constraint;
import javax.validation.GroupSequence;
import javax.validation.Payload;
import javax.validation.groups.Default;
import java.lang.annotation.*;

/**
 * Created by ttnd on 3/10/16.
 */

@Constraint(validatedBy = {ProjectCreateValidator.class})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,ElementType.TYPE})
@Retention( value = RetentionPolicy.RUNTIME)
@Documented
@GroupSequence({Default.class,Create.class})
public @interface ProjectCreate {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
