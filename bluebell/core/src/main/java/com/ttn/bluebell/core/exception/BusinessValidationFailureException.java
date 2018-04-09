package com.ttn.bluebell.core.exception;

/**
 * Created by ttnd on 28/9/16.
 */
public class BusinessValidationFailureException extends BaseException {
    public BusinessValidationFailureException() {
    }

    public BusinessValidationFailureException(String message) {
        super(message);
    }

    public BusinessValidationFailureException(Throwable cause) {
        super(cause);
    }
}
