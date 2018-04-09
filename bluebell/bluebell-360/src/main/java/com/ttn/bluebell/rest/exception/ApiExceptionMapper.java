package com.ttn.bluebell.rest.exception;

import com.google.gson.Gson;
import com.ttn.bluebell.durable.model.common.Fault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ApiExceptionMapper{

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionMapper.class);

    // Catch All Exception
    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String internalError(Exception exception) {
        logger.error("ErrorMessage = {} ", exception.getLocalizedMessage(), exception);
        List<Fault> faultList = new ArrayList<>();
        String code = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        String expMessage = exception.getLocalizedMessage();
        faultList.add(new Fault(CustomisedExceptionMessageLoader.getPropertyValue(StringUtils.isEmpty(expMessage)?"exception.server.error":expMessage), code));
        return new Gson().toJson(faultList);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String validationException( MethodArgumentNotValidException e) {
        logger.error("ErrorMessage = {} ", e.getLocalizedMessage(), e);
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        List<Fault> faultList = new ArrayList<Fault>();
        String code = HttpStatus.BAD_REQUEST.getReasonPhrase();
        for(ObjectError error:errors){
            Fault fault = new Fault(error.getDefaultMessage(),code, Collections.emptyMap());
            faultList.add(fault);
        }
        return new Gson().toJson(faultList);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String validationException( HttpMessageConversionException e) {
        //logger.error(">>>>."+e.get+">>>>>>>>>>>>");
        logger.error("ErrorMessage = {} ", e.getLocalizedMessage(), e);
        String code = HttpStatus.BAD_REQUEST.getReasonPhrase();
        List<Fault> faultList = new ArrayList<>();
        faultList.add(new Fault("Invalid Input !", code));
        return new Gson().toJson(faultList);
    }

}
