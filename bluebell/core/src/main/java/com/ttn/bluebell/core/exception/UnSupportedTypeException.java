package com.ttn.bluebell.core.exception;

/**
 * Created by praveshsaini on 23/9/16.
 */
public class UnSupportedTypeException extends BaseException {
    public UnSupportedTypeException() {
    }

    public UnSupportedTypeException(String message) {
        super(message);
    }

    public UnSupportedTypeException(Throwable cause) {
        super(cause);
    }
}
