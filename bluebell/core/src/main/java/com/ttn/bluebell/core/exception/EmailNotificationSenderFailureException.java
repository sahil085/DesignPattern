package com.ttn.bluebell.core.exception;

/**
 * Created by ttn on 30/9/16.
 */
public class EmailNotificationSenderFailureException extends BaseException {

    public EmailNotificationSenderFailureException(String message) {
        super(message);
    }

    public EmailNotificationSenderFailureException(Throwable cause) {
        super(cause);
    }
}
