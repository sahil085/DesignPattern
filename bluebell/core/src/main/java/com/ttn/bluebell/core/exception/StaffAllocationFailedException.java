package com.ttn.bluebell.core.exception;

/**
 * Created by praveshsaini on 10/10/16.
 */
public class StaffAllocationFailedException extends BaseException {
    public StaffAllocationFailedException() {
    }

    public StaffAllocationFailedException(String message) {
        super(message);
    }

    public StaffAllocationFailedException(Throwable cause) {
        super(cause);
    }
}
