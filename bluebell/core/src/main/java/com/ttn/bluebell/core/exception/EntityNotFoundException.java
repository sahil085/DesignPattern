package com.ttn.bluebell.core.exception;

/**
 * Created by praveshsaini on 23/9/16.
 */
public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
