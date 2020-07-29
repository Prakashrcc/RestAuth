package com.pks.demo.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class RestExceptionHandler {
	@ExceptionHandler(value= {RestRequestException.class})
	public ResponseEntity<Object> handle(RestRequestException e){
	RestException restException=	new RestException(e.getMessage(), HttpStatus.BAD_REQUEST , LocalDateTime.now());
	return new ResponseEntity<Object>(restException,HttpStatus.BAD_REQUEST);
	}

}
