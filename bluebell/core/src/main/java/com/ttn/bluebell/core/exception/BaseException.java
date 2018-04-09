package com.ttn.bluebell.core.exception;

/**
 * Created by praveshsaini on 23/9/16.
 */
public class BaseException extends RuntimeException {
    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
