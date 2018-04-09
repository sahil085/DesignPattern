package com.ttn.bluebell.durable.model.validation.validators;

import com.ttn.bluebell.durable.model.validation.StartDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ttnd on 30/9/16.
 */
public class StartDateValidator implements ConstraintValidator<StartDate,Date> {

    @Override
    public void initialize(StartDate startDate) {
    }

    @Override
    public boolean isValid(Date startDate, ConstraintValidatorContext constraintValidatorContext) {

       /* constraintValidatorContext.disableDefaultConstraintViolation();

        constraintValidatorContext.buildConstraintViolationWithTemplate("failure").addConstraintViolation();
       */ Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 1);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        c.set(Calendar.MINUTE,0);

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.set(Calendar.HOUR, 1);
        start.set(Calendar.SECOND,0);
        start.set(Calendar.MILLISECOND,0);
        start.set(Calendar.MINUTE,0);

        if ( start.get(Calendar.YEAR)>=c.get(Calendar.YEAR) && start.get(Calendar.MONTH)>=c.get(Calendar.MONTH) &&
                start.get(Calendar.DATE)>=c.get(Calendar.DATE))
            return true;
        else
            return false;

    }
}


