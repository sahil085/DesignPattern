package com.ttn.bluebell.durable.model.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Created by arpit on 28/12/17.
 */
public class BBResponse{
    HttpStatus httpStatus;
    Map data;
    String successMessage;
    String errorMessage;
    String warningMessage;
}
