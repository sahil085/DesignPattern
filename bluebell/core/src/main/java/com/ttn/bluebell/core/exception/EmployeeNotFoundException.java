package com.ttn.bluebell.core.exception;

/**
 * Created by praveshsaini on 7/10/16.
 */
public class EmployeeNotFoundException extends BaseException {
    public EmployeeNotFoundException() {
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException(Throwable cause) {
        super(cause);
    }
}
