package com.ttn.bluebell.core.exception;

/**
 * Created by praveshsaini on 23/9/16.
 */
public class UnSupportedOperationException extends BaseException {
    public UnSupportedOperationException() {
    }

    public UnSupportedOperationException(String message) {
        super(message);
    }

    public UnSupportedOperationException(Throwable cause) {
        super(cause);
    }
}
