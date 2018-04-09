package com.ttn.bluebell.rest.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by praveshsaini on 10/10/16.
 */
public class AuthenticationFailureException extends AuthenticationException {

    public AuthenticationFailureException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationFailureException(String msg) {
        super(msg);
    }

}
