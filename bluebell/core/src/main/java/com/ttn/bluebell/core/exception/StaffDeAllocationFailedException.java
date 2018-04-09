package com.ttn.bluebell.core.exception;

/**
 * Created by praveshsaini on 10/10/16.
 */
public class StaffDeAllocationFailedException extends BaseException {
    public StaffDeAllocationFailedException() {
    }

    public StaffDeAllocationFailedException(String message) {
        super(message);
    }

    public StaffDeAllocationFailedException(Throwable cause) {
        super(cause);
    }
}
